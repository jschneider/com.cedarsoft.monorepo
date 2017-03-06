package com.cedarsoft.swing.common;

import javax.annotation.Nonnull;
import java.net.URL;

/**
 * Offers utility methods to load icons
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class IconUtils {
  /**
   * @noinspection NullableProblems
   */
  @Nonnull
  public static final ClassLoader CLASS_LOADER;

  static {
    ClassLoader classLoader = IconUtils.class.getClassLoader();
    if (classLoader == null) {
      throw new IllegalStateException("No class loader found");
    }
    CLASS_LOADER = classLoader;
  }

  @Nonnull
  public static URL getResource(@Nonnull String path) {
    URL resource = CLASS_LOADER.getResource(path);
    if (resource == null) {
      throw new IllegalArgumentException("Could not find resource for <" + path + ">");
    }
    return resource;
  }

}
