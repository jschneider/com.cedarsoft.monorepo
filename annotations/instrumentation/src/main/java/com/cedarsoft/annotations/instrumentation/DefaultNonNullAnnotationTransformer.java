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

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 */
public class DefaultNonNullAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  protected final String nonNullReturnValueCode;
  @Nonnull
  protected final String nonNullParamCode;

  public DefaultNonNullAnnotationTransformer( @WillClose @Nonnull InputStream nonNullReturnValueCodeInput, @WillClose @Nonnull InputStream nonNullParamCodeInput ) throws IOException {
    try {
      nonNullReturnValueCode = new String( ByteStreams.toByteArray( nonNullReturnValueCodeInput ), Charsets.UTF_8 );
    } finally {
      nonNullReturnValueCodeInput.close();
    }

    try {
      nonNullParamCode = new String( ByteStreams.toByteArray( nonNullParamCodeInput ), Charsets.UTF_8 );
    } finally {
      nonNullReturnValueCodeInput.close();
    }
  }

  public DefaultNonNullAnnotationTransformer( @Nonnull String nonNullReturnValueCode, @Nonnull String nonNullParamCode ) {
    this.nonNullReturnValueCode = nonNullReturnValueCode;
    this.nonNullParamCode = nonNullParamCode;
  }

  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException {
    for ( CtMethod method : ctClass.getDeclaredMethods() ) {
      if ( method.isEmpty() ) {
        continue;
      }

      //Checking for return values
      //noinspection NegativelyNamedBooleanVariable
      boolean nonNullAnnotation = isAnnotated( method, Nonnull.class );
      boolean nullAnnotation = isAnnotated( method, Nullable.class );

      if ( nonNullAnnotation && nullAnnotation ) {
        throw new IllegalStateException( "Must not have both annotations in " + ctClass.getName() + "#" + method.getName() + ": " + Nonnull.class.getName() + " and " + Nullable.class.getName() );
      }

      if ( nonNullAnnotation && !method.getReturnType().isPrimitive() ) {
        insertAssertedVerificationCodeAfter( method, getNonNullReturnValueCode() );
      }

      //Now let's check for the parameters
      transformParameters( method );
    }


    //Transform the parameters for the constructors
    for ( CtConstructor constructor : ctClass.getConstructors() ) {
      transformParameters( constructor );
    }
  }

  protected void transformParameters( @Nonnull CtBehavior method ) throws ClassNotFoundException, NotFoundException, CannotCompileException {
    Object[][] parameterAnnotations = method.getParameterAnnotations();
    for ( int i = 0, parameterAnnotationsLength = parameterAnnotations.length; i < parameterAnnotationsLength; i++ ) {
      if ( AnnotationUtils.hasAnnotation( parameterAnnotations[i], Nonnull.class ) ) {
        //Skip primitive parameters
        if ( method.getParameterTypes()[i].isPrimitive() ) {
          continue;
        }

        int parameterNumber = i + 1;
        String format = MessageFormat.format( getNonNullParamCode(), parameterNumber );
        insertAssertedVerificationCodeBefore( method, format );
      }
    }
  }

  @Nonnull
  protected String getNonNullParamCode() {
    return nonNullParamCode;
  }

  @Nonnull
  protected String getNonNullReturnValueCode() {
    return nonNullReturnValueCode;
  }
}
