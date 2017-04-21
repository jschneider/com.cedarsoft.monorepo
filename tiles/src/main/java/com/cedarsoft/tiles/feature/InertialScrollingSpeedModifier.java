package com.cedarsoft.tiles.feature;

import com.cedarsoft.unit.other.px_ns;

import javax.annotation.Nonnull;

/**
 * Can be used to modify the inertial scrolling speed
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface InertialScrollingSpeedModifier {
  /**
   * Default implementation that does not limit the speed at all
   */
  @Nonnull
  InertialScrollingSpeedModifier NONE = new InertialScrollingSpeedModifier() {
    @Override
    public double calculateSpeedX(@px_ns double speed) {
      return speed;
    }

    @Override
    public double calculateSpeedY(@px_ns double speed) {
      return speed;
    }
  };

  /**
   * Returns the scrolling speed for the x axis
   */
  @px_ns
  double calculateSpeedX(@px_ns double speed);

  /**
   * Returns the scrolling speed for the y axis
   */
  @px_ns
  double calculateSpeedY(@px_ns double speed);
}
