package com.cedarsoft.commons.javafx.converter;

import javafx.util.StringConverter;

/**
 * Converter that simply returns the string itself.
 * Can be used to avoid null checks with converters
 */
public class String2StringConverter extends StringConverter<String> {
  @Override
  public String toString(String object) {
    return object;
  }

  @Override
  public String fromString(String string) {
    return string;
  }
}
