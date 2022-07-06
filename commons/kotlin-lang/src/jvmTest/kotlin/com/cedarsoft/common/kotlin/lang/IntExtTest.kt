package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class IntExtTest {
  @Test
  fun testIt() {
    val value = 0b010101

    assertThat(value.isBitSet(0)).isTrue()
    assertThat(value.isBitSet(1)).isFalse()

    assertThat(value.countTrailingZeros()).isEqualTo(0)

    assertThat(0b010110.countTrailingZeros()).isEqualTo(1)
    assertThat(0b010100.countTrailingZeros()).isEqualTo(2)

    assertThat(0b0.countTrailingZeros()).isEqualTo(32)
  }
}
