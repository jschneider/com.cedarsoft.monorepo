package com.cedarsoft.swing.common;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Swing related methods
 */
public class SwingHelper {
  private SwingHelper() {
  }

  /**
   * Throws an exception if the method is not called from EDT
   */
  public static void assertEdt() throws IllegalThreadStateException {
    if (SwingUtilities.isEventDispatchThread()) {
      return;
    }

    throw new IllegalThreadStateException("Must be called in EDT. But was called in thread <" + Thread.currentThread().getName() + ">");
  }

  /**
   * Returns the graphics configuration for the default screen device
   */
  @AnyThread
  @Nonnull
  public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = env.getDefaultScreenDevice();
    return device.getDefaultConfiguration();
  }

  /**
   * Returns the graphics configuration that should be used.
   * It is either the last used or the default graphics configuration
   */
  @AnyThread
  @Nonnull
  public static GraphicsConfiguration getCurrentGraphicsConfiguration() {
    @Nullable GraphicsConfiguration lastUsedGraphicsConfiguration = getLastUsedGraphicsConfiguration();
    if (lastUsedGraphicsConfiguration != null) {
      return lastUsedGraphicsConfiguration;
    }

    return getDefaultGraphicsConfiguration();
  }

  @AnyThread
  @Nonnull
  private static final AtomicReference<GraphicsConfiguration> LAST_USED_GRAPHICS_CONFIGURATION = new AtomicReference<>();

  /**
   * Sets the last used graphics configuration
   */
  @AnyThread
  public static void setLastUsedGraphicsConfiguration(@Nonnull GraphicsConfiguration deviceConfiguration) {
    LAST_USED_GRAPHICS_CONFIGURATION.set(deviceConfiguration);
  }

  /**
   * Returns the last used graphics configuration if there is one
   */
  @AnyThread
  @Nullable
  public static GraphicsConfiguration getLastUsedGraphicsConfiguration() {
    @Nullable GraphicsConfiguration lastUsedGraphicsConfiguration = LAST_USED_GRAPHICS_CONFIGURATION.get();
    if (lastUsedGraphicsConfiguration == null) {
      return null;
    }

    return lastUsedGraphicsConfiguration;
  }

  /**
   * Clears a volatile image
   */
  public static void clearVolatileImage(@Nonnull VolatileImage volatileImage) {
    Graphics2D graphics = (Graphics2D) volatileImage.getGraphics();
    try {
      graphics.setComposite(AlphaComposite.DstOut);
      graphics.fillRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());
    } finally {
      graphics.dispose();
    }
  }

  /**
   * Creates a new volatile image
   */
  @Nonnull
  public static VolatileImage createVolatileImage(int width, int height) {
    if (width <= 0) {
      throw new IllegalArgumentException("Invalid width <" + width + ">");
    }

    if (height <= 0) {
      throw new IllegalArgumentException("Invalid height <" + height + ">");
    }

    return getCurrentGraphicsConfiguration().createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
  }

  /**
   * Enables the anti aliasing for a given graphics context
   */
  public static void enableAntialiasing(@Nonnull Graphics2D graphics2D) {
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
  }

  /**
   * Runs the given runnable in a background thread using a swing worker
   */
  @AnyThread
  @NonBlocking
  public static void runInBackground(@Nonnull Runnable runnable) {
    new SwingWorker<Void, Void>() {
      @Nullable
      @NonUiThread
      @Override
      protected Void doInBackground() throws Exception {
        runnable.run();
        return null;
      }
    }.execute();
  }

}