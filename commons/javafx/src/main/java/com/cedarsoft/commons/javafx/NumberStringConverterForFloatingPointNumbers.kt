package com.cedarsoft.commons.javafx

import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat
import java.util.Locale

/**
 * A [NumberStringConverter] for floating point values only.
 *
 * @author Christian Erbelding ([ce@cedarsoft.com](mailto:ce@cedarsoft.com))
 */
class NumberStringConverterForFloatingPointNumbers(
  private val doubleNumberFormat: () -> NumberFormat
) : NumberStringConverter() {

  constructor(doubleNumberFormat: NumberFormat) : this({ doubleNumberFormat })

  @JvmOverloads
  constructor(locale: Locale = Locale.getDefault()) : this(NumberFormat.getNumberInstance(locale))

  override fun getNumberFormat(): NumberFormat {
    return doubleNumberFormat()
  }

  override fun fromString(value: String): Number {
    return if ("-" == value) {
      0.0
    } else {
      super.fromString(value)
    }
  }
}
