package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 *
 */
public class BaseUnit {
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
  public String getSymbol() {
    return symbol;
  }
}
