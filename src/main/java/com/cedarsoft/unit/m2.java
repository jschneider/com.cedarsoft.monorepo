package com.cedarsoft.unit;

import com.cedarsoft.quantity.Area;
import com.cedarsoft.quantity.SiDerivedUnit;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * A square metre
 *
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Area
@SiDerivedUnit( Area.class )
public @interface m2 {
  @NotNull
  @NonNls
  String SYMBOL = "m²";

  @NotNull
  @NonNls
  String NAME = "square metre";
}
