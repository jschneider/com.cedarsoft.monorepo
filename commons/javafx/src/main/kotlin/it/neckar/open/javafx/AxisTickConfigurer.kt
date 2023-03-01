package it.neckar.open.javafx

import javafx.beans.binding.Bindings
import javafx.scene.chart.NumberAxis

/**
 * GlobalTilesCache * @see AxisConfigurer
 */
object AxisTickConfigurer {
  /**
   * Computes the tick unit in accordance to the current [NumberAxis.lowerBoundProperty] and [NumberAxis.upperBoundProperty] of the given axis.
   */
  @JvmStatic
  fun bindTickUnitToBounds(axis: NumberAxis) {
    axis.tickUnitProperty().bind(Bindings.createDoubleBinding({
      val lowerBound = axis.lowerBoundProperty().get()
      val upperBound = axis.upperBoundProperty().get()
      if (java.lang.Double.isNaN(lowerBound) || java.lang.Double.isNaN(upperBound)) {
        return@createDoubleBinding 1.0
      }
      val range = upperBound - lowerBound
      getTickCount(range)
    }, axis.lowerBoundProperty(), axis.upperBoundProperty()))
  }

  @JvmStatic
  fun getTickCount(range: Double): Double {
    val positiveRange = Math.abs(range)
    if (positiveRange == 0.0) {
      return 1.0
    }
    val powerOfTen = Math.pow(10.0, Math.floor(Math.log10(positiveRange))).toInt() // 3145 -> 3000, 27 -> 10, 534 -> 100 etc
    val limit = (positiveRange / powerOfTen).toInt() * powerOfTen
    return Math.max(1.0, limit * 0.1)
  }

  @JvmStatic
  fun configureAxisTick(axis: NumberAxis, upperLimit: Double) {
    axis.tickUnit = getTickCount(upperLimit)
  }
}
