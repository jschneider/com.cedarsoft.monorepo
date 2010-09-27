package com.cedarsoft;

import com.cedarsoft.quantity.Quantity;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 *
 */
public class BaseUnit<Q extends Quantity<Q>> implements Unit<Q> {
  @NotNull
  @NonNls
  private final String symbol;
  @NotNull
  private final Class<? extends Annotation> unitAnnotationType;

  public BaseUnit( @NotNull @NonNls String symbol, @NotNull Class<? extends Annotation> unitAnnotationType ) {
    this.symbol = symbol;
    this.unitAnnotationType = unitAnnotationType;
  }

  @NotNull
  public Class<? extends Annotation> getUnitAnnotationType() {
    return unitAnnotationType;
  }

  @NonNls
  @NotNull
  @Override
  public String getSymbol() {
    return symbol;
  }
}
