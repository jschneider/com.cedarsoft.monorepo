package com.cedarsoft.commons.javafx

import javafx.util.StringConverter

/**
 * Base class for converters that only implement the toString method
 */
abstract class OneWayConverter<T> : StringConverter<T>() {
  override fun fromString(string: String?): T {
    throw UnsupportedOperationException()
  }

  override fun toString(value: T): String {
    if (value == null) {
      return ""
    }

    return format(value)
  }

  /**
   * Formats the given object
   */
  abstract fun format(toFormat: T): String
}
