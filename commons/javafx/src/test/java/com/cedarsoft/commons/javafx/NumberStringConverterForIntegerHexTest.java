package com.cedarsoft.commons.javafx;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

class NumberStringConverterForIntegerHexTest {

  @Test
  public void name() {
    NumberStringConverterForIntegerHex converter = new NumberStringConverterForIntegerHex();
    assertThat(converter.fromString("-").longValue()).isEqualTo(0);
    assertThat(converter.fromString("-0").longValue()).isEqualTo(0);
    assertThat(converter.fromString("+0").longValue()).isEqualTo(0);
    assertThat(converter.fromString("0").longValue()).isEqualTo(0);
    assertThat(converter.fromString("4").longValue()).isEqualTo(4);
  }
}
