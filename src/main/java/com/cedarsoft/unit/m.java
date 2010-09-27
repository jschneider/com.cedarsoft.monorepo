package com.cedarsoft.unit;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.quantity.SIBaseUnit;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Represents a metre
 *
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Length
@SIBaseUnit( Length.class )
public @interface m {
  @NotNull
  @NonNls
  String SYMBOL = "m";
}
