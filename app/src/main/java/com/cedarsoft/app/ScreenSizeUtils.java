package com.cedarsoft.app;

import org.jetbrains.annotations.Nullable;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

/**
 *
 */
public class ScreenSizeUtils {
  private ScreenSizeUtils() {
  }

  /**
   * Returns the size of the first screen size
   *
   * @return the size of the first screen
   */
  @Nullable
  public static Dimension getScreenSize() throws HeadlessException {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screenDevices = graphicsEnvironment.getScreenDevices();
    DisplayMode displayMode = screenDevices[0].getDisplayMode();

    return new Dimension( displayMode.getWidth(), displayMode.getHeight() );
  }
}
