package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

/**
 * Helper class to translate enum values
 */
@FunctionalInterface
public interface EnumTranslator {
  /**
   * Gets the text for the given enum
   */
  @Nonnull
  String translate(@Nonnull Enum<?> item);
}
