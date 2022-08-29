package com.cedarsoft.commons.javafx

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.scene.chart.NumberAxis
import javax.annotation.Nonnull

/**
 * @see AxisTickConfigurer
 */
object AxisConfigurer {
  /**
   * Sets the lower bound of the given axis in accordance to the given property.
   */
  @JvmStatic
  fun bindLowerBound(@Nonnull axis: NumberAxis, @Nonnull numberProperty: Property<Number?>) {
    numberProperty.addListener { observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? -> decreaseLowerBound(axis, newValue) }
    decreaseLowerBound(axis, numberProperty.value)
  }

  /**
   * Sets the upper bound of the given axis in accordance to the given property.
   */
  @JvmStatic
  fun bindUpperBound(@Nonnull axis: NumberAxis, @Nonnull numberProperty: Property<Number?>) {
    numberProperty.addListener { observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? -> increaseUpperBound(axis, newValue) }
    increaseUpperBound(axis, numberProperty.value)
  }

  /**
   * Sets the lower bound of the given axis to it includes at least the given maximum value.
   */
  @JvmStatic
  fun decreaseLowerBound(@Nonnull axis: NumberAxis, maxValue: Number?) {
    if (maxValue == null) {
      return
    }
    val d = maxValue.toDouble()
    val maxValueMinus1Percent = d - Math.abs(d * 0.01)
    if (maxValueMinus1Percent < axis.lowerBound) {
      val maxValueMinus10Percent = d - Math.abs(d * 0.1)
      val tickUnit = AxisTickConfigurer.getTickCount(maxValueMinus10Percent)
      axis.lowerBound = Math.floor(maxValueMinus10Percent / tickUnit) * tickUnit
    }
  }

  /**
   * Sets the upper bound of the given axis to it includes at least the given minimum value.
   */
  @JvmStatic
  fun increaseUpperBound(@Nonnull axis: NumberAxis, minValue: Number?) {
    if (minValue == null) {
      return
    }
    val d = minValue.toDouble()
    val minValuePlus1Percent = d + Math.abs(d * 0.01)
    if (minValuePlus1Percent > axis.upperBound) {
      val minValuePlus10Percent = d + Math.abs(d * 0.1)
      val tickUnit = AxisTickConfigurer.getTickCount(minValuePlus10Percent)
      axis.upperBound = Math.ceil(minValuePlus10Percent / tickUnit) * tickUnit
    }
  }
}
