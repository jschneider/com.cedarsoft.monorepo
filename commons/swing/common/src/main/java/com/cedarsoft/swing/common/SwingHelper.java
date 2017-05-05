package com.cedarsoft.swing.common;

import com.cedarsoft.annotations.AnyThread;
import com.cedarsoft.annotations.NonBlocking;
import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.components.CJideButton;
import com.google.common.collect.Lists;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.ButtonStyle;
import com.jidesoft.utils.PortingUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

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


  /**
   * Returns the focused frame.
   * Falls back to the main frame if no frame has the focus.
   */
  @UiThread
  @Nullable
  public static JFrame getFrameSafe() {
    try {
      return getFocusedFrame();
    } catch (IllegalStateException ignore) {
    }

    try {
      //If there is just a single frame, return that
      return getSingleFrame();
    } catch (IllegalStateException ignore) {
    }

    return null;
  }

  /**
   * Returns the frame if there is just one
   */
  @Nullable
  public static JFrame getSingleFrame() {
    List<Frame> frames = Lists.newArrayList(Frame.getFrames());
    if (frames.isEmpty()) {
      throw new IllegalStateException("No frame found");
    }

    if (frames.size() > 1) {
      throw new IllegalStateException("More than one frame found");
    }

    return (JFrame) frames.get(0);
  }

  /**
   * Returns the focused frame.
   */
  @UiThread
  @Nonnull
  public static JFrame getFocusedFrame() throws IllegalStateException {
    return getFrame(frame -> frame.hasFocus() || frame.getFocusOwner() != null)
      .orElseThrow(new Supplier<RuntimeException>() {
                     @Override
                     public RuntimeException get() {
                       throw new IllegalStateException("No frame found");
                     }
                   }
      );
  }

  /**
   * Returns (any) frame for the given filter.
   *
   * @throws IllegalStateException if {@link Frame#getFrames()} returns an empty list, i.e. if the application has not created any frames.
   */
  @UiThread
  @Nonnull
  public static Optional<JFrame> getFrame(@Nonnull Predicate<JFrame> filter) throws IllegalStateException {
    List<Frame> frames = Lists.newArrayList(Frame.getFrames());
    if (frames.isEmpty()) {
      throw new IllegalStateException("No frame found");
    }

    return frames
      .stream()
      .filter(frame -> frame instanceof JFrame)
      .map(frame -> (JFrame) frame)
      .filter(filter)
      .findAny();
  }

  @UiThread
  @Nullable
  public static Component findRoot(@Nullable Component parent) {
    if (parent == null) {
      return null;
    }
    return SwingUtilities.getWindowAncestor(parent);
  }

  /**
   * Creates a hyperlink
   */
  @Nonnull
  public static CJideButton createHyperLink(@Nonnull Action clickAction) {
    CJideButton link = new CJideButton(clickAction);
    link.setButtonStyle(ButtonStyle.HYPERLINK_STYLE);
    link.setForeground(new Color(26, 24, 195));
    link.setBorder(null);
    return link;
  }

  /**
   * Creates a popup with the given esc action
   */
  @Nonnull
  @UiThread
  public static JidePopup createPopup(@Nonnull Action escAction) {
    return createPopup(escAction, null);
  }

  @Nonnull
  @UiThread
  public static JidePopup createPopup(@Nonnull Action escAction, @Nullable PopupHiddenCallback callback) {
    JidePopup popup = new JidePopup() {
      @Override
      public void hidePopupImmediately(boolean cancelled) {
        super.hidePopupImmediately(cancelled);

        if (callback != null) {
          callback.popupHidden(this, cancelled);
        }
      }
    };

    popup.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
    popup.getRootPane().getActionMap().put("ESCAPE", escAction);
    return popup;
  }

  @UiThread
  public static void showPopupOver(@Nonnull JidePopup popup, @Nonnull JComponent owner) {
    Point locationOnScreen = owner.getLocationOnScreen();
    popup.setOwner(owner);
    showPopupOnScreen(popup, locationOnScreen.x, locationOnScreen.y);
  }

  /**
   * Shows the popup on the screen
   *
   * @param popup      the popup
   * @param preferredX the preferred top x location
   * @param preferredY the preferred top y location
   */
  @UiThread
  public static void showPopupOnScreen(@Nonnull JidePopup popup, int preferredX, int preferredY) {
    Rectangle popupRect = new Rectangle(new Point(preferredX, preferredY), popup.getPreferredSize());
    Rectangle screenBounds = PortingUtils.getContainingScreenBounds(popupRect, true);

    if (popupRect.getMaxX() > screenBounds.getMaxX()) {
      popupRect.x = (int) (screenBounds.getMaxX() - popupRect.getWidth());
    }

    if (popupRect.getMaxY() > screenBounds.getMaxY()) {
      popupRect.y = (int) (screenBounds.getMaxY() - popupRect.getHeight());
    }

    popup.showPopup(popupRect.x, popupRect.y, null);
  }

  @FunctionalInterface
  public interface PopupHiddenCallback {
    void popupHidden(@Nonnull JidePopup popup, boolean cancelled);
  }
}
