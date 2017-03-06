package com.cedarsoft.swing.common;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.Scaled;
import com.cedarsoft.unit.other.ScalingFactor;
import com.cedarsoft.unit.other.Unscaled;

/**
 * Scales the UI
 */
@UiThread
public class UiScaler {
  /**
   * The scaling factor
   */
  @ScalingFactor
  private static double FACTOR = 1.0;

  private UiScaler() {
  }

  @UiThread
  public static void setFactor(@ScalingFactor double factor) {
    UiScaler.FACTOR = factor;
  }

  @UiThread
  @ScalingFactor
  public static double getFactor() {
    return FACTOR;
  }

  @Scaled
  public static int scale(@Unscaled int value) {
    return (int) Math.round(value * getFactor());
  }

  @Scaled
  public static int scaleFloor(@Unscaled int value) {
    return (int) Math.floor(value * getFactor());
  }

  @Scaled
  public static int scaleCeil(@Unscaled int value) {
    return (int) Math.ceil(value * getFactor());
  }

  @Unscaled
  public static int reverse(@Scaled int widthScaled) {
    return (int) Math.round(widthScaled / getFactor());
  }
}
