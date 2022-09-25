package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class DoublesProviderTest {
  @Test
  fun testFixedSize() {
    val provider = DoublesProvider.fixedSize(7, object : MultiDoublesProvider<Int> {
      override fun valueAt(index: Int): Double {
        return index * 10.0
      }
    })

    assertThat(provider.size()).isEqualTo(7)
    assertThat(provider.valueAt(4)).isEqualTo(40.0)
    assertThat(provider.valueAt(7)).isEqualTo(70.0)
  }
}
