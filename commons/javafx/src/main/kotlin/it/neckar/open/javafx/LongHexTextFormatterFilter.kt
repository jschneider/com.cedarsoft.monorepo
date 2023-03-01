package it.neckar.open.javafx

import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid longs are added
 */
class LongHexTextFormatterFilter(
  converter: NumberStringConverterForLongHex,
  filter: Predicate<Long>,
) : NumberTextFormatterFilter(
  converter = converter,
  filter = Predicate { number: Number -> filter.test(number.toLong()) },
  emptyValue = 0
)
