package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import com.cedarsoft.commons.provider.Index
import com.cedarsoft.commons.provider.IndexProvider
import org.junit.jupiter.api.Test

class IndexProviderTest {
  @Test
  fun testEmpty() {
    assertThat(IndexProvider.empty<MyIndex>().size()).isEqualTo(0)

    IndexProvider.forValues(MyIndex(1), MyIndex(3), MyIndex(5)).let { indexProvider ->
      assertThat(indexProvider.size()).isEqualTo(3)
      assertThat(indexProvider.valueAt(0)).isEqualTo(MyIndex(1))
      assertThat(indexProvider.valueAt(1)).isEqualTo(MyIndex(3))
      assertThat(indexProvider.valueAt(2)).isEqualTo(MyIndex(5))
    }
  }
}

@JvmInline
value class MyIndex(override val value: Int) : Index {

}
