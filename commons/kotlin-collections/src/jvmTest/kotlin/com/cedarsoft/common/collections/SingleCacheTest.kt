package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class SingleCacheTest {
  @Test
  fun testIt() {
    val cache = SingleCache<String>()
    assertThat(cache.get()).isNull()

    assertThat(cache.getOrStore { "asdf" }).isEqualTo("asdf")
    assertThat(cache.get()).isEqualTo("asdf")

    assertThat(cache.getOrStore { "bla" }).isEqualTo("asdf")
  }
}
