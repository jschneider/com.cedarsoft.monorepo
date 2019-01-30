package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for long values only, in hex representation
 */
public class NumberStringConverterForIntegerHex extends NumberStringConverter {

  @Nonnull
  private final NumberFormat integerHexNumberFormat;

  public NumberStringConverterForIntegerHex() {
    integerHexNumberFormat = new HexNumberFormat();
  }

  @Nonnull
  @Override
  protected NumberFormat getNumberFormat() {
    return integerHexNumberFormat;
  }

  @Override
  public Number fromString(String value) {
    if ("-".equals(value)) {
      return 0;
    }

    if (value == null) {
      return null;
    }

    if (value.startsWith("0x")) {
      return Integer.parseInt(value.substring(2), 16);
    }
    return Integer.parseInt(value, 16);
  }
}
