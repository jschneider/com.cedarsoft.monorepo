package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

/**
 * Marker interface for classes the provide an i18n key.
 * That key can be used to be translated with a RES instance
 */
@FunctionalInterface
public interface HasI18nKey {
  /**
   * Returns the i18n key for this object
   */
  @Nonnull
  String getI18nKey();
}
