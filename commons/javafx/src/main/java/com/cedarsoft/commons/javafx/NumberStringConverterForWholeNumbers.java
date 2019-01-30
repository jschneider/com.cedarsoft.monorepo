package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for integer and long values.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class NumberStringConverterForWholeNumbers extends NumberStringConverter {
  @Nonnull
  private final NumberFormat wholeNumberFormat; // works for Integers _and_ Longs

  public NumberStringConverterForWholeNumbers() {
    this(Locale.getDefault());
  }

  public NumberStringConverterForWholeNumbers(@Nonnull Locale locale) {
    wholeNumberFormat = NumberFormat.getIntegerInstance(locale); // works for Integers _and_ Longs
  }

  @Nonnull
  @Override
  protected NumberFormat getNumberFormat() {
    return wholeNumberFormat;
  }

  @Override
  public Number fromString(String value) {
    if ("-".equals(value)) {
      return 0;
    }
    return super.fromString(value);
  }
}
