/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.annotations.instrumentation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.DuplicateMemberException;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractAnnotationTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform( @Nonnull ClassLoader loader, @Nonnull String className, @Nonnull Class<?> classBeingRedefined, @Nonnull ProtectionDomain protectionDomain, @Nonnull byte[] classfileBuffer ) throws IllegalClassFormatException {
    try {
      final CtClass ctClass = AbstractAnnotationTransformer.getCtClass( loader, classBeingRedefined.getName() );

      if (ctClass.isInterface()) {
        return classfileBuffer;
      }

      transformClass( ctClass );
      return ctClass.toBytecode();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static final String ASSERTION_DISABLED_FIELD_NAME = "$assertionsDisabled";

  protected abstract void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException;

  protected static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<? extends Annotation> annotationType ) throws ClassNotFoundException {
    return method.hasAnnotation(annotationType);
  }

  @Nonnull
  protected static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = new ClassPool(true);
    pool.appendClassPath(new LoaderClassPath(loader));

    return pool.get(className);
  }

  protected static void insertAssertedVerificationCodeBefore( @Nonnull CtBehavior method, @Nonnull String verificationCode ) throws CannotCompileException {
    method.insertBefore( wrapInAssertion(method.getDeclaringClass(), verificationCode ) );
  }

  protected static void insertAssertedVerificationCodeAfter( @Nonnull CtMethod method, @Nonnull String verificationCode ) throws CannotCompileException {
    method.insertAfter(wrapInAssertion(method.getDeclaringClass(), verificationCode));
  }

  /**
   * Wraps the given code into an assertion statement
   *
   * @param ctClass the class the assertion status is queried for
   * @param code the assertion code
   * @return the assert statement
   */
  @Nonnull
  protected static String wrapInAssertion(@Nonnull CtClass ctClass, @Nonnull String code) throws CannotCompileException {
    ensureAssertField(ctClass);

    return "if( !" + ASSERTION_DISABLED_FIELD_NAME + " ){" + code + "}";
  }

  /**
   * Ensures that the assert field for the class exists
   *
   * @param ctClass the class
   */
  protected static void ensureAssertField( @Nonnull CtClass ctClass ) throws CannotCompileException {
    try {
      CtField assertionsDisabledField = CtField.make("static final boolean " + ASSERTION_DISABLED_FIELD_NAME + " = !" + ctClass.getName() + ".class.desiredAssertionStatus();", ctClass);
      //Ensure the field is marked as synthetic
      assertionsDisabledField.setModifiers(AccessFlag.of(AccessFlag.SYNTHETIC | assertionsDisabledField.getModifiers()));
      ctClass.addField(assertionsDisabledField);
    } catch (DuplicateMemberException ignore) {
    }
  }
}
