package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.si.microM;
import com.cedarsoft.unit.si.mm;
import com.cedarsoft.unit.si.ns;
import com.cedarsoft.unit.si.s;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;

public class Units {

  private Units() {
  }


  @Nonnull
  public static DoubleBinding microMeter2mm(@Nonnull DoubleProperty doubleProperty) {
    return Bindings.createDoubleBinding(() -> microMeter2mm(doubleProperty.get()), doubleProperty);
  }

  @Nonnull
  public static DoubleBinding mm2microMeter(@Nonnull DoubleProperty doubleProperty) {
    return Bindings.createDoubleBinding(() -> mm2microMeter(doubleProperty.get()), doubleProperty);
  }

  /**
   * Converts a micro meter value to milli meter
   */
  @mm
  public static double microMeter2mm(@microM double value) {
    //noinspection FloatingPointEquality
    if (value == Double.MIN_VALUE) {
      return Double.MIN_VALUE;
    }
    //noinspection FloatingPointEquality
    if (value == -Double.MIN_VALUE) {
      return -Double.MIN_VALUE;
    }

    return value / 1000.0;
  }

  /**
   * Converts a milli meter value to micro meter
   */
  @microM
  public static double mm2microMeter(@mm double value) {
    //noinspection FloatingPointEquality
    if (value == Double.MIN_VALUE) {
      return Double.MIN_VALUE;
    }
    //noinspection FloatingPointEquality
    if (value == -Double.MIN_VALUE) {
      return -Double.MIN_VALUE;
    }

    return value * 1000.0;
  }

  /**
   * Converts nano seconds to seconds as double
   */
  @s
  public static double nanos2secs(@ns long value) {
    return value / 1_000.0 / 1_000.0 / 1_000.0;
  }
}
