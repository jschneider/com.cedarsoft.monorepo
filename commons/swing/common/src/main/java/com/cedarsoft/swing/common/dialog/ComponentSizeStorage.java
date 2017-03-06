package com.cedarsoft.swing.common.dialog;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.UiScaler;
import com.cedarsoft.unit.other.Scaled;
import com.cedarsoft.unit.other.Unscaled;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Supports storage of sizes of components
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ComponentSizeStorage {
  @Nonnull
  public static final String HEIGHT_SUFFIX = ".height";
  @Nonnull
  public static final String WIDTH_SUFFIX = ".width";

  @Nonnull
  private final String backendKey;
  @Nonnull
  private final Backend backend;

  private ComponentSizeStorage(@Nonnull Component component, @Nonnull String backendKey, @Nonnull Backend backend) {
    this.backendKey = backendKey;
    this.backend = backend;

    registerListeners(component);
  }

  private void registerListeners(final Component component) {
    component.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        backend.store(backendKey + WIDTH_SUFFIX, UiScaler.reverse(component.getWidth()));
        backend.store(backendKey + HEIGHT_SUFFIX, UiScaler.reverse(component.getHeight()));
      }
    });
  }

  @Nullable
  @UiThread
  @Scaled
  public Dimension get() {
    @Unscaled Integer preferredWidth = backend.get(backendKey + WIDTH_SUFFIX);
    @Unscaled Integer preferredHeight = backend.get(backendKey + HEIGHT_SUFFIX);
    if (preferredWidth == null || preferredHeight == null) {
      return null;
    }
    @Scaled Dimension size = new Dimension();
    size.width = UiScaler.scale(preferredWidth);
    size.height = UiScaler.scale(preferredHeight);
    return size;
  }

  @Nonnull
  @Scaled
  @UiThread
  public Dimension get(@Unscaled int defaultWidth, @Unscaled int defaultHeight) {
    Dimension size = new Dimension();
    size.width = UiScaler.scale(backend.get(backendKey + WIDTH_SUFFIX, defaultWidth));
    size.height = UiScaler.scale(backend.get(backendKey + HEIGHT_SUFFIX, defaultHeight));
    return size;
  }

  @Nonnull
  public static ComponentSizeStorage connect(@Nonnull Component component, @Nonnull Backend backend, @Nonnull Class<?> keyClass) {
    return connect(component, backend, keyClass, null);
  }

  @Nonnull
  public static ComponentSizeStorage connect(@Nonnull Component component, @Nonnull Backend backend, @Nonnull Class<?> keyClass, @Nullable String additionalKey) {
    String key = keyClass.getName();
    if (additionalKey != null) {
      key = key + '.' + additionalKey;
    }
    return new ComponentSizeStorage(component, key, backend);
  }

  /**
   * Backend for storing/retrieving stored values
   */
  public interface Backend {

    /**
     * Stores the given value
     */
    void store(@Nonnull String key, @Unscaled int value);

    /**
     * Returns null if no value is found for the given key
     */
    @Unscaled
    @Nullable
    Integer get(@Nonnull String key);

    /**
     * Returns the value or default value if no value is found
     */
    @Unscaled
    int get(@Nonnull String key, int defaultValue);
  }
}
