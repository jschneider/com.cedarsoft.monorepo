package com.cedarsoft.commons.javafx

/**
 * Marker interface for classes the provide an i18n key.
 * That key can be used to be translated with a RES instance
 */
@FunctionalInterface
interface HasI18nKey {
  /**
   * Returns the i18n key for this object
   */
  val i18nKey: String
}
