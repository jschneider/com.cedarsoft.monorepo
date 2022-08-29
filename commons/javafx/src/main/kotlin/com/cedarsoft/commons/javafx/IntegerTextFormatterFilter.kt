package com.cedarsoft.commons.javafx

import javafx.util.converter.NumberStringConverter
import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid integers are added
 */
class IntegerTextFormatterFilter(
  converter: NumberStringConverter,
  filter: Predicate<Int>,
) : NumberTextFormatterFilter(
  converter = converter,
  filter = Predicate { number: Number -> filter.test(number.toInt()) },
  emptyValue = 0
)
