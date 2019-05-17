package com.cedarsoft.commons.javafx.axis

import com.cedarsoft.commons.javafx.properties.*
import com.cedarsoft.unit.other.px
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Side
import javafx.scene.chart.ValueAxis
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.floor

/**
 * An axis that shows only the rounded numbers
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class RoundedNumberAxis
@JvmOverloads constructor(
  lower: Double = 0.0,
  upper: Double = 100.0,
  @px val horizontalTickSpace: Double = 50.0,
  @px val verticalTickSpace: Double = 25.0

) : ValueAxis<Number>(lower, upper) {

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

  override fun getTickMarkLabel(value: Number?): String {
    if (value == null) {
      return ""
    }

    return tickLabelFormatter.format(value)
  }

  override fun calculateTickValues(length: Double, range: Any?): List<Number> {
    val tickCount = calculateTickCount(width, height, side)
    return RoundedAxisTickCalculator(lowerBound, upperBound).calculateTickValues(tickCount)
  }

  override fun calculateMinorTickMarks(): MutableList<Number> {
    return mutableListOf()
  }

  private fun calculateTickCount(@px width: Double, @px height: Double, side: Side): Int {
    if (side.isHorizontal) {
      return floor(width / horizontalTickSpace).toInt()
    }

    if (side.isVertical) {
      return floor(height / verticalTickSpace).toInt()
    }

    throw IllegalArgumentException("Invalid side <$side>")
  }
}
