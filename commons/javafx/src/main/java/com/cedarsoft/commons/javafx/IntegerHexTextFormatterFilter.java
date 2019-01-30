package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Processes text formatter changes. Ensures only valid integers are added
 */
class IntegerHexTextFormatterFilter extends NumberTextFormatterFilter {

  IntegerHexTextFormatterFilter(@Nonnull NumberStringConverterForIntegerHex converter, @Nonnull Predicate<Integer> filter) {
    super(converter, number -> filter.test(number.intValue()), 0);
  }

}
