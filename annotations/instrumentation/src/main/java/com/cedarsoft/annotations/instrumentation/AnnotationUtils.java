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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utils classes for annotation related stuff
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AnnotationUtils {
  private AnnotationUtils() {
  }

  /**
   * Returns whether the given annotations contains the searched annotation
   * @param annotations a array of annotations
   * @param annotationType the annotation type
   * @return true if an annotation of the given type is contained, false otherwise
   */
  public static boolean hasAnnotation( @Nonnull Object[] annotations, @Nonnull Class<? extends Annotation> annotationType ) {
    for ( Object annotation : annotations ) {
      if ( isOfType( ( Annotation ) annotation, annotationType ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns whether the given annotation is of the queried type
   * @param annotation the annotation
   * @param expectedAnnotationType the expected annotation type
   * @return true if the annotation is of the given type, false otherwise
   */
  public static boolean isOfType( @Nonnull Annotation annotation, @Nonnull Class<? extends Annotation> expectedAnnotationType ) {
    return isOfType(annotation, expectedAnnotationType.getName());
  }

  public static boolean isOfType(@Nonnull Annotation annotation, @Nonnull String expectedAnnotationTypeName) {
    //if ( annotation.annotationType().getName().equals( expectedAnnotationType.getName() ) ) {
    return annotation.annotationType().getName().equals(expectedAnnotationTypeName);
  }

  /**
   * Returns the (first) annotation of the given type, or null
   *
   * @param annotations the annotations
   * @param annotationType the annotation type
   * @return the annotation - if found - or null
   */
  @Nullable
  public static <T extends Annotation> T findAnnotation(@Nonnull Annotation[] annotations, @Nonnull Class<T> annotationType) {
    return annotationType.cast(findAnnotation(annotations, annotationType.getName()));
  }

  /**
   * Returns the (first) annotation of the given type name, or null
   */
  @Nullable
  public static Annotation findAnnotation(@Nonnull Annotation[] annotations, @Nonnull String annotationTypeName) {
    for (Annotation annotation : annotations) {
      if (isOfType(annotation, annotationTypeName)) {
        return annotation;
      }
    }
    return null;
  }

  /**
   * Returns the value of the annotation using reflection
   */
  public static Object getValueByReflection(@Nonnull Annotation annotation) {
    try {
      Method method = annotation.annotationType().getDeclaredMethod("value");
      return method.invoke(annotation);
    }
    catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
