package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Processes text formatter changes. Ensures only valid doubles are added
 */
public class DoubleTextFormatterFilter extends NumberTextFormatterFilter {

  public DoubleTextFormatterFilter(@Nonnull NumberStringConverterForFloatingPointNumbers converter, @Nonnull Predicate<Double> filter) {
    super(converter, number -> filter.test(number.doubleValue()), 0.0);
  }

}
