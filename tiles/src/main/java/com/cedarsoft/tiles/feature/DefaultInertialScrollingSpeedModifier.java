package com.cedarsoft.tiles.feature;

import com.cedarsoft.unit.other.px_ns;
import com.cedarsoft.unit.other.px_s;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation for scrolling speed calculator
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DefaultInertialScrollingSpeedModifier implements InertialScrollingSpeedModifier {
  /**
   * Default
   */
  @Nonnull
  public static final InertialScrollingSpeedModifier DEFAULT = new DefaultInertialScrollingSpeedModifier(4000);

  @px_ns
  private final double limitX;
  @px_ns
  private final double limitY;

  public DefaultInertialScrollingSpeedModifier(@px_s int pixelsPerSecond) {
    this((double) pixelsPerSecond / TimeUnit.SECONDS.toNanos(1));
  }

  public DefaultInertialScrollingSpeedModifier(@px_ns double limit) {
    this(limit, limit);
  }

  public DefaultInertialScrollingSpeedModifier(@px_ns double limitX, @px_ns double limitY) {
    this.limitX = limitX;
    this.limitY = limitY;
  }

  @Override
  public double calculateSpeedX(@px_ns double speed) {
    return Math.max(Math.min(limitX, speed), -limitX);
  }

  @Override
  public double calculateSpeedY(@px_ns double speed) {
    return Math.max(Math.min(limitY, speed), -limitY);
  }
}
