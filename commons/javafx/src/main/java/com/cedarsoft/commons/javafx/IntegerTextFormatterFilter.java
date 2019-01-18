package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Processes text formatter changes. Ensures only valid integers are added
 */
class IntegerTextFormatterFilter extends NumberTextFormatterFilter {

  IntegerTextFormatterFilter(@Nonnull NumberStringConverterForIntegers converter, @Nonnull Predicate<Integer> filter) {
    super(converter, number -> filter.test(number.intValue()), 0);
  }

}
