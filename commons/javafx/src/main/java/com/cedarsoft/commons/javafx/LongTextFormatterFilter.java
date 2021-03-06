package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * Processes text formatter changes. Ensures only valid longs are added
 */
class LongTextFormatterFilter extends NumberTextFormatterFilter {

  LongTextFormatterFilter(@Nonnull NumberStringConverter converter, @Nonnull Predicate<Long> filter) {
    super(converter, number -> filter.test(number.longValue()), 0);
  }

}
