package it.neckar.open.javafx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NumberStringConverterForIntegerHexTest {
  @Test
  fun name() {
    val converter = NumberStringConverterForIntegerHex()
    assertThat(converter.fromString("-")!!.toLong()).isEqualTo(0)
    assertThat(converter.fromString("-0")!!.toLong()).isEqualTo(0)
    assertThat(converter.fromString("+0")!!.toLong()).isEqualTo(0)
    assertThat(converter.fromString("0")!!.toLong()).isEqualTo(0)
    assertThat(converter.fromString("4")!!.toLong()).isEqualTo(4)
  }
}
