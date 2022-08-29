package com.cedarsoft.commons.javafx

import javafx.util.converter.NumberStringConverter
import java.util.function.Predicate

/**
 * Processes text formatter changes. Ensures only valid floats are added
 */
class FloatTextFormatterFilter(
  converter: NumberStringConverter,
  filter: Predicate<Float>,
) : NumberTextFormatterFilter(
  converter = converter,
  filter = Predicate { number: Number -> filter.test(number.toFloat()) },
  emptyValue = 0.0
)
