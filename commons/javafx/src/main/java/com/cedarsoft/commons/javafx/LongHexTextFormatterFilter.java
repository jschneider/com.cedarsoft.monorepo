package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Processes text formatter changes. Ensures only valid longs are added
 */
class LongHexTextFormatterFilter extends NumberTextFormatterFilter {

  LongHexTextFormatterFilter(@Nonnull NumberStringConverterForLongHex converter, @Nonnull Predicate<Long> filter) {
    super(converter, number -> filter.test(number.longValue()), 0);
  }

}
