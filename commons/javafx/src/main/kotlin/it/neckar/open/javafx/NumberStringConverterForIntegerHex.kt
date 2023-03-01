package it.neckar.open.javafx

import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat

/**
 * A [NumberStringConverter] for long values only, in hex representation
 */
class NumberStringConverterForIntegerHex : NumberStringConverter() {
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
      value.substring(2).toInt(16)
    } else value.toInt(16)
  }
}
