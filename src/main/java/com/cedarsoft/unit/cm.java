package com.cedarsoft.unit;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.quantity.SiDerivedUnit;
import com.cedarsoft.unit.prefix.Prefixed;
import com.cedarsoft.unit.prefix.centi;
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
@Inherited

@Length
@SiDerivedUnit( Length.class )
@Prefixed( centi.class )
@centi
public @interface cm {
  @NotNull
  @NonNls
  String SYMBOL = "cm";

  @NotNull
  @NonNls
  String NAME = "centi metre";
}
