package it.neckar.open.formatting

import it.neckar.open.i18n.I18nConfiguration
import kotlin.text.Typography.nbsp

/**
 * Formats a percentage value using a delegate.
 * Multiplies the given value with 100 and formats the value
 * using the delegate. Appends a percentage sign ("%").
 */
@Deprecated("use asPercentageFormat - as soon as precision has been removed!")
class PercentageFormat(val delegate: NumberFormat) : NumberFormat {
  override fun format(value: Double, i18nConfiguration: I18nConfiguration): String {
    return delegate.format(value * 100.0, i18nConfiguration) + "$nbsp%"
  }

  override val precision: Double
    get() = delegate.precision / 100.0
}

/**
 * Uses this as
 */
fun NumberFormat.asPercentageFormat(): WithUnitFormat {
  return this.withConversion { it * 100.0 }.appendUnit("%")
}
