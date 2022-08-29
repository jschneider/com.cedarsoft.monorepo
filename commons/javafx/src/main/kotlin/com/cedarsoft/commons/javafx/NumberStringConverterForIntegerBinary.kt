package com.cedarsoft.commons.javafx

import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat
import java.text.ParseException

/**
 * A [NumberStringConverter] for integer values, in binary representation
 */
class NumberStringConverterForIntegerBinary(minTextLength: Int) : NumberStringConverter() {

  private val binaryNumberFormat = BinaryNumberFormat(minTextLength)

  override fun getNumberFormat(): NumberFormat {
    return binaryNumberFormat
  }

  override fun fromString(value: String?): Number? {
    if ("-" == value) {
      return 0
    }
    return if (value == null) {
      null
    } else try {
      binaryNumberFormat.parse(value)
    } catch (e: ParseException) {
      throw NumberFormatException(e.toString())
    }
  }
}
