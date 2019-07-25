package com.cedarsoft.commons.javafx

/**
 * Helper class to translate enum values
 */
@FunctionalInterface
interface EnumTranslator {
  /**
   * Gets the text for the given enum
   */
  fun translate(item: Enum<*>): String
}
