package it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class FilteredMultiProviderTest {
  @Test
  fun testSkip() {
    val provider = SizedProvider.forList(listOf("a", "b", "c"))

    val filteredMultiProvider = SkipWithNullSizedProvider(provider) {
      true
    }

    assertThat(filteredMultiProvider.valueAt(0)).isEqualTo("a")
  }

  @Test
  fun testSkip2() {
    val provider = SizedProvider.forList(listOf("a", "b", "c", null))

    val filteredMultiProvider = SkipWithNullSizedProvider(provider) {
      it % 2 == 0
    }

    assertThat(filteredMultiProvider.valueAt(0)).isEqualTo("a")
    assertThat(filteredMultiProvider.valueAt(1)).isEqualTo(null)
    assertThat(filteredMultiProvider.valueAt(2)).isEqualTo("c")
    assertThat(filteredMultiProvider.valueAt(3)).isEqualTo(null)
  }
}
