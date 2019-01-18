package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.scene.paint.Color;

/**
 * A utility class around {@link Color}.
 */
public class ColorUtil {

  private ColorUtil() {
    // a utility class
  }

  @Nonnull
  public static String toRGB(@Nonnull Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  @Nonnull
  public static String toRGBA(@Nonnull Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ", " + color.getOpacity() + ")";
  }

}
