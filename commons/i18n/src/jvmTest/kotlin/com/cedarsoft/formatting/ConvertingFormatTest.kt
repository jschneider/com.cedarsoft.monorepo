package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class ConvertingFormatTest {
  @Test
  fun testIt() {
    ConvertingFormat(percentageFormat) {
      it / 2.0
    }.let {
      assertThat(it.format(1.7)).isEqualTo("85.00 %")
    }
  }

  @Test
  fun testAPI() {
    decimalFormat2digits.withConversion {
      it / 2.0
    }.appendUnit("bar").let {
      assertThat(it.format(1.7)).isEqualTo("0.85 bar")
    }
  }
}
