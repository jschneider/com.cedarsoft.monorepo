package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class _FastArrayIteratorsKtTest {
  @Test
  fun testasdf() {
    val a = DoubleArrayList(7)

    assertThat(a.size).isEqualTo(0)

    a.add(10.0)
    a.add(11.0)
    a.add(12.0)
    a.add(13.0)
    a.add(14.0)
    a.add(15.0)
    a.add(16.0)

    var count = 0

    a.fastForEach {
      count++
    }

    assertThat(count).isEqualTo(7)
  }
}
