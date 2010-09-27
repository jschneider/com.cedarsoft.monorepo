package com.cedarsoft.quantity;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface SiDerivedUnit {
  /**
   * The quantity
   *
   * @return the quantity
   */
  Class<? extends Annotation> value();
}
