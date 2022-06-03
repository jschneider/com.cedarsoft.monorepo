package com.cedarsoft.commons.javafx

import javafx.util.StringConverter

/**
 * Base class for converters that only implement the toString method
 */
abstract class OneWayConverter<T> : StringConverter<T>() {
  override fun fromString(string: String?): T {
    throw UnsupportedOperationException()
  }

  override fun toString(`object`: T): String {
    if (`object` == null) {
      return ""
    }

    return format(`object`)
  }

  /**
   * Formats the given object
   */
  abstract fun format(toFormat: T): String
}
