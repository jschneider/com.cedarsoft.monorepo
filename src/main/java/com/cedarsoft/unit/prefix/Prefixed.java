package com.cedarsoft.unit.prefix;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Prefixed {
  private Prefixed() {
  }


  public static double getFactor( @NotNull Class<? extends Annotation> prefixClass ) {
    Prefix prefix = prefixClass.getAnnotation( Prefix.class );
    if ( prefix == null ) {
      throw new IllegalArgumentException( prefixClass.getName() + " is not annotated with " + Prefix.class.getName() );
    }

    return prefix.value();
  }
}
