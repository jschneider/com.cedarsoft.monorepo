package com.cedarsoft.commons.provider.com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.kotlin.lang.parseInt
import com.cedarsoft.commons.provider.MultiProvider
import com.cedarsoft.commons.provider.SizedProvider
import com.cedarsoft.commons.provider.SortedSizedProvider
import com.cedarsoft.commons.provider.sorted
import com.cedarsoft.commons.provider.wrapMultiProvider
import org.junit.jupiter.api.Test

class SortedSizedProviderTest {
  @Test
  fun testSimple() {
    val unsorted = SizedProvider.forValues("17", "22", "5", "9", "1")
    assertThat(unsorted.size()).isEqualTo(5)

    assertThat(unsorted.toList()).containsExactly("17", "22", "5", "9", "1")

    val sorted = unsorted.sorted(compareBy { it.parseInt() })

    assertThat(sorted.size()).isEqualTo(5)

    assertThat(unsorted.toList()).containsExactly("17", "22", "5", "9", "1")
    assertThat(sorted.toList()).containsExactly("1", "5", "9", "17", "22")

    //Mapped indices have now been updated
    assertThat(sorted.mapped2Original(0)).isEqualTo(4) //--> 1
    assertThat(sorted.mapped2Original(4)).isEqualTo(1) //--> 22
  }

  @Test
  fun testUseForMultiProvider() {
    val unsorted = SizedProvider.forValues("17", "22", "5", "9", "1")
    val sorted = SortedSizedProvider(unsorted, compareBy { it.parseInt() })

    assertThat(unsorted.toList()).containsExactly("17", "22", "5", "9", "1")
    assertThat(sorted.toList()).containsExactly("1", "5", "9", "17", "22")

    val multiProvider = MultiProvider<Int, String> { "value_$it" }
    assertThat(multiProvider.valueAt(0)).isEqualTo("value_0")
    assertThat(multiProvider.valueAt(7)).isEqualTo("value_7")

    val sortedMultiProvider = sorted.wrapMultiProvider<Int, Int, String>(multiProvider)

    assertThat(sortedMultiProvider.toList(5)).containsExactly("value_4", "value_2", "value_3", "value_0", "value_1")
  }
}

fun <T> MultiProvider<*, T>.toList(size: Int): List<T> {
  return List(size) {
    valueAt(it)
  }
}

/**
 * Collects the values into a list.
 * ATTENTION: Only use for tests!
 */
fun <T> SizedProvider<T>.toList(): List<T> {
  return List(this.size()) {
    valueAt(it)
  }
}
