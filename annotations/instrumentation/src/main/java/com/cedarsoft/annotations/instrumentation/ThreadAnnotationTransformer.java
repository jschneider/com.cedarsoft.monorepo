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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.meta.ThreadDescribingAnnotation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ThreadAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  private static String getThreadVerificationCode(@Nonnull List<? extends String> threadDescriptions) {
    StringBuilder paramsBuilder = new StringBuilder();

    paramsBuilder.append("new String[]{");

    for (Iterator<? extends String> iterator = threadDescriptions.iterator(); iterator.hasNext(); ) {
      String threadDescription = iterator.next();
      paramsBuilder.append("\"").append(threadDescription).append("\"");
      if (iterator.hasNext()) {
        paramsBuilder.append(",");
      }
    }
    paramsBuilder.append("}");

    return "com.cedarsoft.annotations.verification.VerifyThread.verifyThread(" + paramsBuilder + ");";
  }

  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException {
    for ( CtMethod method : ctClass.getDeclaredMethods() ) {
      if ( method.isEmpty() ) {
        continue;
      }

      //Look for each annotation - are they thread related?
      List<String> threadDescriptions = new ArrayList<>();

      for (Object annotation : method.getAnnotations()) {
        Class<? extends Annotation> annotationType = ((Annotation) annotation).annotationType();

        //Now find the annotation of the annotation
        @Nullable Annotation threadDescribingAnnotation = AnnotationUtils.findAnnotation(annotationType.getAnnotations(), ThreadDescribingAnnotation.class.getName());
        if (threadDescribingAnnotation == null) {
          continue;
        }

        threadDescriptions.add((String) AnnotationUtils.getValueByReflection(threadDescribingAnnotation));
      }

      //Skip if no relevant annotations have been found
      if (threadDescriptions.isEmpty()) {
        continue;
      }

      insertAssertedVerificationCodeBefore(method, getThreadVerificationCode(threadDescriptions));
    }
  }
}
