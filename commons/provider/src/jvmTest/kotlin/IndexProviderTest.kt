package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class IndexProviderTest {
  @Test
  fun testEmpty() {
    assertThat(IndexProvider.empty().size()).isEqualTo(0)

    IndexProvider.forValues(1, 3, 5).let { indexProvider ->
      assertThat(indexProvider.size()).isEqualTo(3)
      assertThat(indexProvider.valueAt(0)).isEqualTo(1)
      assertThat(indexProvider.valueAt(1)).isEqualTo(3)
      assertThat(indexProvider.valueAt(2)).isEqualTo(5)
    }
  }
}
