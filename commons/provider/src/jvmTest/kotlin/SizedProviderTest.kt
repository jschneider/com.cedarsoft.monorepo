package it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class SizedProviderTest {
  @Test
  fun testMapped() {
    val list = listOf(0, 1, 2, 3)

    val sizedProvider = SizedProvider.mapped(list) {
      it.toString()
    }

    assertThat(sizedProvider.size()).isEqualTo(4)
    assertThat(sizedProvider.valueAt(0)).isEqualTo("0")
    assertThat(sizedProvider.valueAt(2)).isEqualTo("2")
  }

  @Test
  fun testSizedProvider1Foreach() {
    val provider = object : SizedProvider1<String, Number> {
      override fun size(param1: Number): Int {
        return 17
      }

      override fun valueAt(index: Int, param1: Number): String {
        return "$index - $param1"
      }
    }

    assertThat(provider.valueAt(12, 12.0)).isEqualTo("12 - 12.0")

    provider.fastForEachIndexed(12.0) { index, value ->
      assertThat(value).isEqualTo("$index - 12.0")
    }
  }
}
