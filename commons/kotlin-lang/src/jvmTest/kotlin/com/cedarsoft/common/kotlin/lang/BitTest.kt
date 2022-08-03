package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class BitTest {
  @Test
  fun testBitset() {
    assertThat(0.isBitSet(0)).isFalse()
    assertThat(0.isBitSet(1)).isFalse()
    assertThat(0.isBitSet(2)).isFalse()
    assertThat(0.isBitSet(3)).isFalse()
    assertThat(0.isBitSet(4)).isFalse()

    assertThat(1.isBitSet(0)).isTrue()
    assertThat(1.isBitSet(1)).isFalse()
    assertThat(1.isBitSet(2)).isFalse()
    assertThat(1.isBitSet(3)).isFalse()
    assertThat(1.isBitSet(4)).isFalse()

    assertThat(2.isBitSet(0)).isFalse()
    assertThat(2.isBitSet(1)).isTrue()
    assertThat(2.isBitSet(2)).isFalse()
    assertThat(2.isBitSet(3)).isFalse()
    assertThat(2.isBitSet(4)).isFalse()
  }

  @Test
  fun testOther() {
    assertThat(0.takeLowestOneBit()).isEqualTo(0)
    assertThat(1.takeLowestOneBit()).isEqualTo(1)
    assertThat(2.takeLowestOneBit()).isEqualTo(2)
    assertThat(3.takeLowestOneBit()).isEqualTo(1)
    assertThat(4.takeLowestOneBit()).isEqualTo(4)
    assertThat(5.takeLowestOneBit()).isEqualTo(1)
    assertThat(8.takeLowestOneBit()).isEqualTo(8)
  }

  @Test
  fun testFindFirstBit() {
    assertThat(0.findLowestOneBit()).isEqualTo(-1)
    assertThat(1.findLowestOneBit()).isEqualTo(0)
    assertThat(2.findLowestOneBit()).isEqualTo(1)
    assertThat(3.findLowestOneBit()).isEqualTo(0)
    assertThat(4.findLowestOneBit()).isEqualTo(2)
    assertThat(5.findLowestOneBit()).isEqualTo(0)
    assertThat(8.findLowestOneBit()).isEqualTo(3)

    assertThat(0b0_1000.findLowestOneBit()).isEqualTo(3)
    assertThat(0b1_0000.findLowestOneBit()).isEqualTo(4)
    assertThat(0b10_0000.findLowestOneBit()).isEqualTo(5)
    assertThat(0b100_0000.findLowestOneBit()).isEqualTo(6)
    assertThat(0b0_1000_0000.findLowestOneBit()).isEqualTo(7)
    assertThat(0b01_0000_0000.findLowestOneBit()).isEqualTo(8)
    assertThat(0b10_0000_0000.findLowestOneBit()).isEqualTo(9)

    assertThat(Int.MAX_VALUE.findLowestOneBit()).isEqualTo(0)
    assertThat((Int.MAX_VALUE - 1).findLowestOneBit()).isEqualTo(1)

    assertThat(Int.MIN_VALUE.findLowestOneBit()).isEqualTo(31)
    assertThat((Int.MIN_VALUE + 1).findLowestOneBit()).isEqualTo(0)
    assertThat((Int.MIN_VALUE + 2).findLowestOneBit()).isEqualTo(1)

    assertThat(0b1_0000_0000_0000_0000_0000_0000.findLowestOneBit()).isEqualTo(24)
    assertThat(0b1000_0000_0000_0000_0000_0000_0000.findLowestOneBit()).isEqualTo(27)
    assertThat(0b100_0000_0000_0000_0000_0000_0000_0000.findLowestOneBit()).isEqualTo(30)
  }
}
