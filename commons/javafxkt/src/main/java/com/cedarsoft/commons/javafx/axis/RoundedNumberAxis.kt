package com.cedarsoft.commons.javafx.axis

import com.cedarsoft.commons.javafx.properties.getValue
import com.cedarsoft.commons.javafx.properties.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.chart.ValueAxis
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * An axis that shows only the rounded numbers
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class RoundedNumberAxis(
  lower: Double = 0.0,
  upper: Double = 100.0

) : ValueAxis<Double>(lower, upper) {

  val tickLabelFormatterProperty: ObjectProperty<NumberFormat> = SimpleObjectProperty<NumberFormat>(DecimalFormat())
  var tickLabelFormatter by tickLabelFormatterProperty

  init {
    isAutoRanging = false
  }

  override fun getRange(): Any? {
    return null
  }

  override fun setRange(range: Any?, animate: Boolean) {
  }

  override fun getTickMarkLabel(value: Double?): String {
    if (value == null) {
      return ""
    }

    return tickLabelFormatter.format(value)
  }

  override fun calculateTickValues(length: Double, range: Any?): List<Double> {
    return RoundedAxisTickCalculator(lowerBound, upperBound).calculateTickValues()
  }

  override fun calculateMinorTickMarks(): MutableList<Double> {
    return mutableListOf()
  }
}
