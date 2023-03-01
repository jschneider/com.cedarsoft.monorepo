package it.neckar.open.javafx

import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid doubles are added
 */
class DoubleTextFormatFilter(
  converter: NumberStringConverterForFloatingPointNumbers,
  filter: Predicate<Double>,
) : NumberTextFormatterFilter(
  converter = converter, filter = Predicate { number: Number -> filter.test(number.toDouble()) }, emptyValue = 0.0
)
