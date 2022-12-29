package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FastIteratorExtKtTest {
  @Test
  fun testContains() {
    assertThat(doubleArrayOf().fastContains(0.0)).isFalse()
    assertThat(doubleArrayOf().fastContains(1.0)).isFalse()

    assertThat(doubleArrayOf(1.0).fastContains(1.0)).isTrue()
    assertThat(doubleArrayOf(0.0, 1.0).fastContains(1.0)).isTrue()
    assertThat(doubleArrayOf(0.0, 1.0, 7.0).fastContains(1.0)).isTrue()
    assertThat(doubleArrayOf(0.0, 1.0, 7.0).fastContains(99.0)).isFalse()
  }
}
