package com.cedarsoft.unit;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface AlternativeSymbols {
  /**
   * The alternative symbols
   *
   * @return the symbols
   */
  @NotNull @NonNls String[] value();
}
