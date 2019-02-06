package com.cedarsoft.commons.javafx;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Processes text formatter changes. Ensures only valid floats are added
 */
public class FloatTextFormatterFilter extends NumberTextFormatterFilter {

  public FloatTextFormatterFilter(@Nonnull NumberStringConverterForFloatingPointNumbers converter, @Nonnull Predicate<Float> filter) {
    super(converter, number -> filter.test(number.floatValue()), 0.0);
  }

}
