package com.cedarsoft.commons.javafx

import com.cedarsoft.unit.si.microM
import com.cedarsoft.unit.si.mm
import com.cedarsoft.unit.si.ns
import com.cedarsoft.unit.si.s
import javafx.beans.binding.Bindings
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.DoubleProperty

/**
 * TODO: Merge with unit stuff
 */
object Units {
  @JvmStatic
  fun microMeter2mm(doubleProperty: DoubleProperty): DoubleBinding {
    return Bindings.createDoubleBinding({ microMeter2mm(doubleProperty.get()) }, doubleProperty)
  }

  @JvmStatic
  fun mm2microMeter(doubleProperty: DoubleProperty): DoubleBinding {
    return Bindings.createDoubleBinding({ mm2microMeter(doubleProperty.get()) }, doubleProperty)
  }

  /**
   * Converts a micro meter value to milli meter
   */
  @JvmStatic
  fun microMeter2mm(value: @microM Double): @mm Double {
    if (value == Double.MIN_VALUE) {
      return Double.MIN_VALUE
    }
    return if (value == -Double.MIN_VALUE) {
      -Double.MIN_VALUE
    } else value / 1000.0
  }

  /**
   * Converts a milli meter value to micro meter
   */
  @JvmStatic
  fun mm2microMeter(value: @mm Double): @microM Double {
    if (value == Double.MIN_VALUE) {
      return Double.MIN_VALUE
    }
    return if (value == -Double.MIN_VALUE) {
      -Double.MIN_VALUE
    } else value * 1000.0
  }

  /**
   * Converts nanoseconds to seconds as double
   */
  @JvmStatic
  fun nanos2secs(value: @ns Long): @s Double {
    return value / 1000.0 / 1000.0 / 1000.0
  }
}
