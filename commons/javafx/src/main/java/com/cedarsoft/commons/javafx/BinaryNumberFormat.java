package com.cedarsoft.commons.javafx;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Formats integers and long values to/from binary string
 */
public class BinaryNumberFormat extends NumberFormat {

  private final int minTextLength;

  public BinaryNumberFormat(int minTextLength) {
    this.minTextLength = minTextLength;
  }

  @Override
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    throw new IllegalArgumentException("doubles not supported.");
  }

  @Override
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    StringBuffer stringBuffer = toAppendTo.append("0b");
    String binaryString = Long.toBinaryString(number);
    int padCount = minTextLength - binaryString.length();
    for (int i = 0; i < padCount; i++) {
      stringBuffer.append('0');
    }
    stringBuffer.append(binaryString);
    return stringBuffer;
  }

  @Override
  public Number parse(String source, ParsePosition parsePosition) {
    long l = 0;
    if (source.startsWith("0b")) {
      try {
        l = Long.parseUnsignedLong(source.substring(2), 2);
      }
      catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return l;
    }

    try {
      l = Long.parseUnsignedLong(source, 2);
    }
    catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return l;
  }
}
