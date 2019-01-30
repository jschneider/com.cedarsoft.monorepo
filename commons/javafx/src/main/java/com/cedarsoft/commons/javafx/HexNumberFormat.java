package com.cedarsoft.commons.javafx;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * formats integers to/from hex string
 */
public class HexNumberFormat extends NumberFormat {
  @Override
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    throw new IllegalArgumentException("doubles not supported.");
  }

  @Override
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    StringBuffer stringBuffer = toAppendTo.append("0x").append(String.format("%08X", number));
    return stringBuffer;
  }

  @Override
  public Number parse(String source, ParsePosition parsePosition) {
    long l = 0;
    if (source.startsWith("0x")) {
      try {
        l = Long.parseLong(source.substring(2), 16);
      }
      catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return l;
    }

    try {
      l = Long.parseLong(source, 16);
    }
    catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return l;
  }
}
