package com.cedarsoft.tiles.feature;


import com.cedarsoft.unit.other.px_ns;

import javax.annotation.Nonnull;

/**
 * Can be used to toggle inertial scolling on and off
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ToggleableInertialScrollingSpeedModifier implements InertialScrollingSpeedModifier {
  @Nonnull
  private final InertialScrollingSpeedModifier delegate;

  private boolean xEnabled;
  private boolean yEnabled;

  public ToggleableInertialScrollingSpeedModifier(@Nonnull InertialScrollingSpeedModifier delegate) {
    this.delegate = delegate;
  }

  public boolean isyEnabled() {
    return yEnabled;
  }

  public void setyEnabled(boolean yEnabled) {
    this.yEnabled = yEnabled;
  }

  public boolean isxEnabled() {
    return xEnabled;
  }

  public void setxEnabled(boolean xEnabled) {
    this.xEnabled = xEnabled;
  }

  public void setEnabled(boolean xEnabled, boolean yEnabled) {
    this.xEnabled = xEnabled;
    this.yEnabled = yEnabled;
  }

  @Override
  @px_ns
  public double calculateSpeedX(@px_ns double speed) {
    if (xEnabled) {
      return delegate.calculateSpeedX(speed);
    }

    return 0;
  }

  @Override
  @px_ns
  public double calculateSpeedY(@px_ns double speed) {
    if (yEnabled) {
      return delegate.calculateSpeedY(speed);
    }

    return 0;
  }
}
