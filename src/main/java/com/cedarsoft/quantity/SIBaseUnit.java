package com.cedarsoft.quantity;

import com.cedarsoft.unit.m;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents one of the seven SI Base units
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface SIBaseUnit {
  /**
   * The quantity
   *
   * @return the quantity
   */
  Class<? extends Annotation> value();
}
