package com.cedarsoft.unit.prefix;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface Prefix {
  /**
   * Represents the factor
   * @return the factor
   */
  double value();
}
