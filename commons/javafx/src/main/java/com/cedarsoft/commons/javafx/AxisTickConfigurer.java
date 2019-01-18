package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.chart.NumberAxis;

public class AxisTickConfigurer {

  private AxisTickConfigurer() {
    // utility method
  }

  @SuppressWarnings("MagicNumber")
  public static double getTickCount(double upperLimit) {
    int powerOfTen = (int) Math.pow(10, Math.floor(Math.log10(upperLimit))); // 3145 -> 3000, 27 -> 10, 534 -> 100 etc
    int limit = (int) (upperLimit / powerOfTen) * powerOfTen;
    return Math.max(1.0, limit * 0.1);
  }

  public static void configureAxisTick(@Nonnull NumberAxis axis, double upperLimit) {
    axis.setTickUnit(getTickCount(upperLimit));
  }
}
