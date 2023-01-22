package com.cedarsoft.commons.javafx

import com.cedarsoft.annotations.JavaFriendly

/**
 * Helper class to translate enum values
 */
@FunctionalInterface
@JavaFriendly
interface EnumTranslator {
  /**
   * Gets the text for the given enum.
   * Returns null if no translation could be found.
   */
  fun translate(item: Enum<*>): String?
}
