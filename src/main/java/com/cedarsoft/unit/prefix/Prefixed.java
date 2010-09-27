package com.cedarsoft.unit.prefix;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Prefixed {
  private Prefixed() {
  }


  public static double getFactor( @NotNull Class<? extends Annotation> unit ) {
    Prefix prefixAnnotation = getPrefixAnnotation( unit );
    if ( prefixAnnotation == null ) {
      throw new IllegalArgumentException( unit.getName() + " is not annotated with " + Prefix.class.getName() );
    }

    return prefixAnnotation.value();
  }

  public static boolean isPrefixed( @NotNull Class<? extends Annotation> unit ) {
    Prefix prefixAnnotation = getPrefixAnnotation( unit );
    return prefixAnnotation != null;
  }

  @Nullable
  public static Prefix getPrefixAnnotation( @NotNull AnnotatedElement unit ) {
    {
      Prefix prefix = unit.getAnnotation( Prefix.class );
      if ( prefix != null ) {
        return prefix;
      }
    }

    for ( Annotation annotation : unit.getAnnotations() ) {
      Prefix prefix = annotation.annotationType().getAnnotation( Prefix.class );
      if ( prefix != null ) {
        return prefix;
      }
    }

    return null;
  }
}
