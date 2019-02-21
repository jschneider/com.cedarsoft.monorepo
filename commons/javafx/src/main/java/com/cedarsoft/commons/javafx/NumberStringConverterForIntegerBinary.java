package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for integer values, in binary representation
 */
public class NumberStringConverterForIntegerBinary extends NumberStringConverter {

  @Nonnull
  private final NumberFormat binaryNumberFormat;

  public NumberStringConverterForIntegerBinary(int minTextLength) {
    binaryNumberFormat = new BinaryNumberFormat(minTextLength);
  }

  @Nonnull
  @Override
  protected NumberFormat getNumberFormat() {
    return binaryNumberFormat;
  }

  @Override
  public Number fromString(String value) {
    if ("-".equals(value)) {
      return 0;
    }

    if (value == null) {
      return null;
    }

    try {
      return binaryNumberFormat.parse(value);
    }
    catch (ParseException e) {
      throw new NumberFormatException(e.toString());
    }
  }
}
