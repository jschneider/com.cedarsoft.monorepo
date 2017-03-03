package com.cedarsoft.unit.exponential;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Exponential( 2 )
public @interface Squared {
  /**
   * The base unit
   * @return the base unit
   */
  Class<? extends Annotation> value();
}
