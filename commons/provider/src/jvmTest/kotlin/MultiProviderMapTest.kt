package it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class MultiProviderMapTest {
  @Test
  fun testMapping() {
    val multiProvider = MultiProvider<Int, String> {
      "v$it"
    }

    assertThat(multiProvider.valueAt(11)).isEqualTo("v11")

    val mapped = multiProvider.mapped {
      it.substring(1).toInt()
    }

    assertThat(mapped.valueAt(99)).isEqualTo(99)
  }
}
