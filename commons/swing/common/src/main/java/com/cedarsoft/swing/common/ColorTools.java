package com.cedarsoft.swing.common;

import com.cedarsoft.unit.other.pct;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;

public class ColorTools {
  private ColorTools() {
  }

  /**
   * Parses a color from a comma separated rgb string
   */
  @Nonnull
  public static Color parseRgbColor(@Nonnull String rgbString) {
    final String[] values = rgbString.split(",");

    if (values.length != 3) {
      throw new IllegalArgumentException("Could not parse rgb string <" + rgbString + ">");
    }

    final int red = Integer.parseInt(values[0].trim());
    final int green = Integer.parseInt(values[1].trim());
    final int blue = Integer.parseInt(values[2].trim());

    return new Color(red, green, blue);
  }


  /**
   * Converts a color to an RGB string
   */
  @Nonnull
  public static String toRgbString(@Nonnull final Color color) {
    return String.valueOf(color.getRed()) + ',' + color.getGreen() + ',' + color.getBlue();
  }

  /**
   * Creates a color that simulates an alpha value
   */
  @Nonnull
  public static Color simulatedAlpha(@Nonnull Color color, @pct double alpha, @Nonnull Color backgroundColor) {
    int newRed = simulateAlpha(color.getRed(), alpha, backgroundColor.getRed());
    int newGreen = simulateAlpha(color.getGreen(), alpha, backgroundColor.getGreen());
    int newBlue = simulateAlpha(color.getBlue(), alpha, backgroundColor.getBlue());

    return new Color(newRed, newGreen, newBlue, 200);
  }

  private static int simulateAlpha(int colorValue, double alpha, int backgroundValue) {
    return (int) (colorValue * alpha + backgroundValue * (1 - alpha));
  }

  private static int brighter(int colorValue, double brightness) {
    return (int) (colorValue * (1 - brightness) + 255 * brightness);
  }

  @Nonnull
  public static Color withAlpha(@Nonnull Color color, @pct double alpha) {
    return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
  }

  @Nonnull
  public static Color brighterWithAlpha(@Nonnull Color color, @pct double brightness, @pct double alpha) {
    return new Color(brighter(color.getRed(), brightness), brighter(color.getGreen(), brightness), brighter(color.getBlue(), brightness), (int) (alpha * 255));
  }

  @Nonnull
  public static Color brighter(@Nonnull Color color, @pct double brightness) {
    return new Color(brighter(color.getRed(), brightness), brighter(color.getGreen(), brightness), brighter(color.getBlue(), brightness), color.getAlpha());
  }

  /**
   * Converts a color to a float value that can be used to sort the color
   */
  public static float color2float(@Nonnull Color color) {
    return ((float) color.getRed() * 0.299f + (float) color.getGreen() * 0.587f + (float) color.getBlue() * 0.114f) / 256f;
  }

  /**
   * Compares two colors that may be nullable
   */
  public static int compare(@Nullable Color color1, @Nullable Color color2) {
    float colorAsFloat1 = color1 == null ? -1 : color2float(color1);
    float colorAsFloat2 = color2 == null ? -1 : color2float(color2);

    return Float.compare(colorAsFloat1, colorAsFloat2);
  }

  /**
   * get the color that matches the percent value between start hue and hue
   */
  @Nonnull
  public static Color interpolate(double percent, double startHue, double endHue) {
    float saturation = 0.35f;
    float brightness = 1.0f;

    return interpolate(percent, startHue, endHue, saturation, brightness);
  }

  @Nonnull
  public static Color interpolate(double percent, double startHue, double endHue, float saturation, float brightness) {
    double range;
    if (endHue > startHue) {
      range = endHue - startHue;
    }
    else {
      range = 1 - (startHue - endHue);
    }

    double hue = percent * range + startHue;

    return Color.getHSBColor((float) hue, saturation, brightness);
  }
}