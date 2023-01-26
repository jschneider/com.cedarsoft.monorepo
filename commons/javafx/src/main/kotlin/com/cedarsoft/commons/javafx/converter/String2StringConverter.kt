package com.cedarsoft.commons.javafx.converter

import javafx.util.StringConverter

/**
 * Converter that simply returns the string itself.
 * Can be used to avoid null checks with converters
 */
class String2StringConverter : StringConverter<String>() {
  override fun toString(value: String): String {
    return value
  }

  override fun fromString(string: String): String {
    return string
  }
}
