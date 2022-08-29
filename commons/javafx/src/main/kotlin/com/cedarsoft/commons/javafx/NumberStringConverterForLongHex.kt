package com.cedarsoft.commons.javafx

import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat

/**
 * A [NumberStringConverter] for long values only, in hex representation
 */
class NumberStringConverterForLongHex : NumberStringConverter() {

  // works for longs, too
  private val integerHexNumberFormat: NumberFormat = HexNumberFormat()

  override fun getNumberFormat(): NumberFormat {
    return integerHexNumberFormat
  }

  override fun fromString(value: String?): Number? {
    if ("-" == value) {
      return 0
    }
    if (value == null) {
      return null
    }
    return if (value.startsWith("0x")) {
      value.substring(2).toLong(16)
    } else value.toLong(16)
  }
}
