package com.cedarsoft.quantity;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface Derived {
  /**
   * The unit this is derived from
   *
   * @return the unit this is derived from
   */
  @NotNull Class<? extends Annotation> from();

  /**
   * The factor
   *
   * @return the factor
   */
  double factor();
}
