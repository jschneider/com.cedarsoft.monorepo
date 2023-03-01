package it.neckar.open.formatting

import it.neckar.open.i18n.I18nConfiguration

/**
 * Number format that appends a unit
 */
class WithUnitFormat(
  /**
   * The delegate that is used for format the value
   */
  val delegate: NumberFormat,

  /**
   * The unit that is appended
   */
  val unit: String,
) : NumberFormat {
  override fun format(value: Double, i18nConfiguration: I18nConfiguration): String {
    return "${delegate.format(value, i18nConfiguration)} $unit"
  }
}

/**
 * Creates a new number format that appends the given unit
 */
fun NumberFormat.appendUnit(unit: String): WithUnitFormat {
  return WithUnitFormat(this, unit)
}
