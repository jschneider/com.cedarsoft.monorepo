package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class FilteredSizedProviderTest {
  @Test
  fun testIt() {
    val unfiltered = SizedProvider.forValues("a", null, "c", "d", "e")
    val filtered = unfiltered.filtered { index: Int, value: String? ->
      value != null
    }

    assertThat(unfiltered.toList()).containsExactly("a", null, "c", "d", "e")

    //Call to size triggers the sorting
    assertThat(filtered.size()).isEqualTo(4)
    assertThat(filtered.toList()).containsExactly("a", "c", "d", "e")
  }

  @Test
  fun testMultiProvider() {
    val unfiltered = SizedProvider.forValues("a", null, "c", "d", "e")
    val filtered: SizedProviderWithIndexMapping<String?> = unfiltered.filtered { index: Int, value: String? ->
      value != null
    }

    assertThat(unfiltered.toList()).containsExactly("a", null, "c", "d", "e")
    assertThat(filtered.toList()).containsExactly("a", "c", "d", "e") //calls size() to recalculate the index

    val multiProvider = MultiProvider.forListModulo<MyIndex1, String>(listOf("A", "null", "B", "C", "D", "E"))
    val multiProviderFiltered = filtered.wrapMultiProvider<MyIndex1, MyIndex2, String?>(multiProvider)

    assertThat(multiProviderFiltered.valueAt(0)).isEqualTo("A")
    assertThat(multiProviderFiltered.valueAt(1)).isEqualTo("B")
    assertThat(multiProviderFiltered.valueAt(2)).isEqualTo("C")
    assertThat(multiProviderFiltered.valueAt(3)).isEqualTo("D")
    assertThat(multiProviderFiltered.valueAt(4)).isEqualTo("A") //modulo
  }

  private annotation class MyIndex1
  private annotation class MyIndex2
}

fun <T> SizedProvider<T>.toList(): List<T> {
  return List(size()) {
    valueAt(it)
  }
}
