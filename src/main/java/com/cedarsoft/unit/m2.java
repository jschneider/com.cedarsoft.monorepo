package com.cedarsoft.unit;

import com.cedarsoft.quantity.Area;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Area
@Inherited
public @interface m2 {
  @NotNull
  @NonNls
  String SYMBOL = "m²";
}
