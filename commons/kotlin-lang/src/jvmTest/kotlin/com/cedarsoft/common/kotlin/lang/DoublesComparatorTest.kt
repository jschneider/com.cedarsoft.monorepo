package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class DoublesComparatorTest {
  @Test
  fun testNatural() {
    ensureCompareNatural(1.0, 2.0)
    ensureCompareNatural(2.0, 2.0)
    ensureCompareNatural(7.0, 8.0)
    ensureCompareNatural(7.0, 6.0)
  }

  private fun ensureCompareNatural(a: Double, b: Double) {
    val nativeComparator = Comparator.naturalOrder<Double>()
    assertThat(DoublesComparator.natural.compare(a, b)).isEqualTo(nativeComparator.compare(a, b))
  }
}
