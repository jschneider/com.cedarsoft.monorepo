package it.neckar.open.provider.it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.DoublesFilter
import it.neckar.open.provider.DoublesProvider
import it.neckar.open.provider.MultiProvider
import it.neckar.open.provider.filtered
import it.neckar.open.provider.toList
import org.junit.jupiter.api.Test

class FilteredDoublesProviderTest {
  @Test
  fun testIt() {
    val unfiltered = DoublesProvider.forDoubles(10.0, Double.NaN, 5.0, 11.0, 1.0)
    val filtered = unfiltered.filtered(DoublesFilter.finite)

    assertThat(unfiltered.toList()).containsExactly(10.0, Double.NaN, 5.0, 11.0, 1.0)

    //Call to size triggers the sorting
    assertThat(filtered.size()).isEqualTo(4)
    assertThat(filtered.toList()).containsExactly(10.0, 5.0, 11.0, 1.0)
  }

  @Test
  fun testMultiProvider() {
    val unfiltered = DoublesProvider.forDoubles(1.0, Double.NaN, 3.0, 4.0, 5.0)
    val filtered = unfiltered.filtered(DoublesFilter.finite)

    val multiProvider = MultiProvider.forListModulo<MyIndex1, String>(listOf("1.0", "NaN", "3.0", "4.0", "5.0"))
    val multiProviderSorted = filtered.wrapMultiProvider<MyIndex1, MyIndex2, String>(multiProvider)

    assertThat(filtered.size()).isEqualTo(4)

    assertThat(multiProvider.valueAt(0)).isEqualTo("1.0")
    assertThat(multiProvider.valueAt(1)).isEqualTo("NaN")

    assertThat(multiProviderSorted.valueAt(0)).isEqualTo("1.0")
    assertThat(multiProviderSorted.valueAt(1)).isEqualTo("3.0")
    assertThat(multiProviderSorted.valueAt(2)).isEqualTo("4.0")
    assertThat(multiProviderSorted.valueAt(3)).isEqualTo("5.0")
  }

  private annotation class MyIndex1
  private annotation class MyIndex2
}
