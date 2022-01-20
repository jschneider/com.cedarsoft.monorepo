package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class PercentageFormatTest {
  @Test
  internal fun testPrecision() {
    // example for 2 maximum fraction digits
    // 0.00001 -> 0.0%
    // 0.0001  -> 0.01%
    // 0.001   -> 0.1%
    // 0.01    -> 1%
    // 0.1     -> 10%
    // 1       -> 100%
    assertThat(PercentageFormat(DecimalFormat(2, 1, 2, true)).precision).isEqualTo(0.0001)
    assertThat(PercentageFormat(DecimalFormat(1, 1, 2, true)).precision).isEqualTo(0.001)
    assertThat(PercentageFormat(DecimalFormat(0, 1, 2, true)).precision).isEqualTo(0.01)
  }

  @Test
  fun testFormat() {
    assertThat((DecimalFormat(2, 1, 1, true).asPercentageFormat()).format(1.2123)).isEqualTo("121.23 %")
    assertThat((DecimalFormat(1, 1, 1, true).asPercentageFormat()).format(1.2123)).isEqualTo("121.2 %")
    assertThat((DecimalFormat(0, 1, 1, true).asPercentageFormat()).format(1.2123)).isEqualTo("121.2 %")
  }
}
