package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class SizedProvider1MapTest {
  @Test
  fun testMapping() {
    val sizedProvider = object : SizedProvider1<String, Any> {
      override fun size(param1: Any): Int {
        return 5
      }

      override fun valueAt(index: Int, param1: Any): String {
        return "v$index"
      }
    }

    assertThat(sizedProvider.valueAt(11, Any())).isEqualTo("v11")

    val mapped = sizedProvider.mapped {
      it.substring(1).toInt()
    }

    assertThat(mapped.valueAt(99, Any())).isEqualTo(99)
  }
}
