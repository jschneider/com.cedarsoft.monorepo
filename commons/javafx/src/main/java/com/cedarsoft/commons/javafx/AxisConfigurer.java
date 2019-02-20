package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javafx.beans.property.Property;
import javafx.scene.chart.NumberAxis;

/**
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 * @see AxisTickConfigurer
 */
public class AxisConfigurer {

  private AxisConfigurer() {
    // utility class
  }

  /**
   * Sets the lower bound of the given axis in accordance to the given property.
   */
  public static void bindLowerBound(@Nonnull NumberAxis axis, @Nonnull Property<Number> numberProperty) {
    numberProperty.addListener((observable, oldValue, newValue) -> decreaseLowerBound(axis, newValue));
    decreaseLowerBound(axis, numberProperty.getValue());
  }

  /**
   * Sets the upper bound of the given axis in accordance to the given property.
   */
  public static void bindUpperBound(@Nonnull NumberAxis axis, @Nonnull Property<Number> numberProperty) {
    numberProperty.addListener((observable, oldValue, newValue) -> increaseUpperBound(axis, newValue));
    increaseUpperBound(axis, numberProperty.getValue());
  }

  /**
   * Sets the lower bound of the given axis to it includes at least the given maximum value.
   */
  public static void decreaseLowerBound(@Nonnull NumberAxis axis, @Nullable Number maxValue) {
    if (maxValue == null) {
      return;
    }
    double d = maxValue.doubleValue();
    final double maxValueMinus1Percent = d - Math.abs(d * 0.01);
    if (maxValueMinus1Percent < axis.getLowerBound()) {
      final double maxValueMinus10Percent = d - Math.abs(d * 0.1);
      double tickUnit = AxisTickConfigurer.getTickCount(maxValueMinus10Percent);
      axis.setLowerBound(Math.floor(maxValueMinus10Percent / tickUnit) * tickUnit);
    }
  }

  /**
   * Sets the upper bound of the given axis to it includes at least the given minimum value.
   */
  public static void increaseUpperBound(@Nonnull NumberAxis axis, @Nullable Number minValue) {
    if (minValue == null) {
      return;
    }
    double d = minValue.doubleValue();
    final double minValuePlus1Percent = d + Math.abs(d * 0.01);
    if (minValuePlus1Percent > axis.getUpperBound()) {
      final double minValuePlus10Percent = d + Math.abs(d * 0.1);
      double tickUnit = AxisTickConfigurer.getTickCount(minValuePlus10Percent);
      axis.setUpperBound(Math.ceil(minValuePlus10Percent / tickUnit) * tickUnit);
    }
  }

}
