package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * Processes text formatter changes. Ensures only valid integers are added
 */
class IntegerTextFormatterFilter extends NumberTextFormatterFilter {

  IntegerTextFormatterFilter(@Nonnull NumberStringConverter converter, @Nonnull Predicate<Integer> filter) {
    super(converter, number -> filter.test(number.intValue()), 0);
  }

}
