package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.beans.binding.Bindings;
import javafx.scene.chart.NumberAxis;

/**
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 * @see AxisConfigurer
 */
public class AxisTickConfigurer {

  private AxisTickConfigurer() {
    // utility method
  }

  /**
   * Computes the tick unit in accordance to the current {@link NumberAxis#lowerBoundProperty()} and {@link NumberAxis#upperBoundProperty()} of the given axis.
   */
  public static void bindTickUnitToBounds(@Nonnull NumberAxis axis) {
    axis.tickUnitProperty().bind(Bindings.createDoubleBinding(() -> {
      final double lowerBound = axis.lowerBoundProperty().get();
      final double upperBound = axis.upperBoundProperty().get();
      if (Double.isNaN(lowerBound) || Double.isNaN(upperBound)) {
        return 1.0;
      }
      final double range = upperBound - lowerBound;
      return AxisTickConfigurer.getTickCount(range);
    }, axis.lowerBoundProperty(), axis.upperBoundProperty()));
  }

  @SuppressWarnings("MagicNumber")
  public static double getTickCount(double range) {
    final double positiveRange = Math.abs(range);
    if (positiveRange == 0) {
      return 1.0;
    }
    int powerOfTen = (int) Math.pow(10, Math.floor(Math.log10(positiveRange))); // 3145 -> 3000, 27 -> 10, 534 -> 100 etc
    int limit = (int) (positiveRange / powerOfTen) * powerOfTen;
    return Math.max(1.0, limit * 0.1);
  }

  public static void configureAxisTick(@Nonnull NumberAxis axis, double upperLimit) {
    axis.setTickUnit(getTickCount(upperLimit));
  }
}
