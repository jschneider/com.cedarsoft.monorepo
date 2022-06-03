package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for integer and long values.
 *
GlobalTilesCache */
public class NumberStringConverterForIntegers extends NumberStringConverter {
  @Nonnull
  private final NumberFormat integerNumberFormat; // works for Integers _and_ Longs

  public NumberStringConverterForIntegers() {
    this(Locale.getDefault());
  }

  public NumberStringConverterForIntegers(@Nonnull Locale locale) {
    integerNumberFormat = NumberFormat.getIntegerInstance(locale); // works for Integers _and_ Longs
  }

  @Nonnull
  @Override
  protected NumberFormat getNumberFormat() {
    return integerNumberFormat;
  }

  @Override
  public Number fromString(String value) {
    if ("-".equals(value)) {
      return 0;
    }
    return super.fromString(value);
  }
}
