package it.neckar.open.javafx

import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid integers are added
 */
internal class IntegerHexTextFormatterFilter(
  converter: NumberStringConverterForIntegerHex,
  filter: Predicate<Int>,
) : NumberTextFormatterFilter(
  converter = converter,
  filter = Predicate { number: Number -> filter.test(number.toInt()) },
  emptyValue = 0
)
