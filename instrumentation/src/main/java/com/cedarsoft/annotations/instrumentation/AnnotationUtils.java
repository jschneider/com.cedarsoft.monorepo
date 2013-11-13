package com.cedarsoft.annotations.instrumentation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

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
   * @param expectedAnnotationType the exprected annotation type
   * @return true if the annotation is of the given type, false otherwise
   */
  public static boolean isOfType( @Nonnull Annotation annotation, @Nonnull Class<? extends Annotation> expectedAnnotationType ) {
    //if ( annotation.annotationType().getName().equals( expectedAnnotationType.getName() ) ) {
    return annotation.annotationType().equals( expectedAnnotationType );
  }


  /**
   * Returns the (first) annotation of the given type, or null
   * @param type the type
   * @param annotationType the annotation type
   * @param <T> the annotation type
   * @return the annotation - if found - or null
   */
  @Nullable
  public static <T extends Annotation> T findAnnotation( @Nonnull Class<?> type, @Nonnull Class<T> annotationType ) {
    for ( Annotation annotation : type.getAnnotations() ) {
      if ( isOfType( annotation, annotationType ) ) {
        return annotationType.cast( annotation );
        //return ( T ) annotation;
      }
    }
    return null;
  }
}
