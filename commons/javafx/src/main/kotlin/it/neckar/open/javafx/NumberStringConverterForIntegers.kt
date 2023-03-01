package it.neckar.open.javafx

import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat
import java.util.Locale
import javax.annotation.Nonnull

/**
 * A [NumberStringConverter] for integer and long values.
 *
 * GlobalTilesCache  */
class NumberStringConverterForIntegers
@JvmOverloads constructor(@Nonnull locale: Locale? = Locale.getDefault()) : NumberStringConverter() {

  // works for Integers _and_ Longs
  private val integerNumberFormat: NumberFormat = NumberFormat.getIntegerInstance(locale)

  override fun getNumberFormat(): NumberFormat {
    return integerNumberFormat
  }

  override fun fromString(value: String?): Number? {
    return if ("-" == value) {
      0
    } else super.fromString(value)
  }
}
