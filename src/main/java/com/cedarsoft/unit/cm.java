package com.cedarsoft.unit;

import com.cedarsoft.quantity.Derived;
import com.cedarsoft.quantity.Length;
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
@Length
@Inherited
@Derived(
  from = m.class,
  factor = 100
)
public @interface cm {
  @NotNull
  @NonNls
  String SYMBOL = "cm";
}
