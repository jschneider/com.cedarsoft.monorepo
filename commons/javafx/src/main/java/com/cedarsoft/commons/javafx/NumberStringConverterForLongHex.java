package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;

import javax.annotation.Nonnull;

import javafx.util.converter.NumberStringConverter;

/**
 * A {@link NumberStringConverter} for long values only, in hex representation
 */
public class NumberStringConverterForLongHex extends NumberStringConverter {

  @Nonnull
  private final NumberFormat integerHexNumberFormat; // works for longs, too

  public NumberStringConverterForLongHex() {
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
      return Long.parseLong(value.substring(2), 16);
    }
    return Long.parseLong(value, 16);
  }
}
