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

  public static boolean hasAnnotation( @Nonnull Object[] annotations, @Nonnull Class<? extends Annotation> annotationType ) {
    for ( Object annotation : annotations ) {
      if ( isOfType( ( Annotation ) annotation, annotationType ) ) {
        return true;
      }
    }
    return false;
  }

  public static boolean isOfType( @Nonnull Annotation annotation, @Nonnull Class<? extends Annotation> expectedAnnotationType ) {
    //if ( annotation.annotationType().getName().equals( expectedAnnotationType.getName() ) ) {
    return annotation.annotationType().equals( expectedAnnotationType );
  }


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
