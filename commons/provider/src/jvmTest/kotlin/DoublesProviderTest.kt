package it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import it.neckar.open.unit.other.Index
import org.junit.jupiter.api.Test

class DoublesProviderTest {
  @Test
  fun testFixedSize() {
    val provider = DoublesProvider.of(7) { index -> index * 10.0 }

    assertThat(provider.size()).isEqualTo(7)
    assertThat(provider.valueAt(4)).isEqualTo(40.0)
    assertThat(provider.valueAt(7)).isEqualTo(70.0)
  }
}
