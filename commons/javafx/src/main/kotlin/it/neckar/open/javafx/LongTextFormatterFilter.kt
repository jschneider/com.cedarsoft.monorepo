package it.neckar.open.javafx

import javafx.util.converter.NumberStringConverter
import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid longs are added
 */
internal class LongTextFormatterFilter(
  converter: NumberStringConverter,
  filter: Predicate<Long>,
) : NumberTextFormatterFilter(
  converter = converter,
  filter = Predicate { number: Number -> filter.test(number.toLong()) },
  emptyValue = 0
)
