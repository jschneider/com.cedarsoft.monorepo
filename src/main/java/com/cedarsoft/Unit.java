package com.cedarsoft;

import com.cedarsoft.quantity.Quantity;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @param <Q>
 */
public interface Unit<Q extends Quantity<Q>> {
  @NotNull
  @NonNls
  String getSymbol();
}