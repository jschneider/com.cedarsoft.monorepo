package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for floating point values only.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class NumberStringConverterForFloatingPointNumbers extends NumberStringConverter {
  @Nonnull
  private final NumberFormat doubleNumberFormat;

  public NumberStringConverterForFloatingPointNumbers() {
    this(Locale.getDefault());
  }

  public NumberStringConverterForFloatingPointNumbers(@Nonnull Locale locale) {
    this(NumberFormat.getNumberInstance(locale));
  }

  public NumberStringConverterForFloatingPointNumbers(@Nonnull NumberFormat numberFormat) {
    doubleNumberFormat = numberFormat;
  }

  @Nonnull
  @Override
  protected NumberFormat getNumberFormat() {
    return doubleNumberFormat;
  }

  @Override
  public Number fromString(String value) {
    if ("-".equals(value)) {
      return 0.0;
    }
    return super.fromString(value);
  }

}
