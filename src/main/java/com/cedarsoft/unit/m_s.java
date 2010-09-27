package com.cedarsoft.unit;

import com.cedarsoft.quantity.SiDerivedUnit;
import com.cedarsoft.quantity.Speed;
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

@Speed
@SiDerivedUnit( Speed.class )
public @interface m_s {
  @NotNull
  @NonNls
  String SYMBOL = "m/s";

  @NotNull
  @NonNls
  String NAME = "metre per second";
}
