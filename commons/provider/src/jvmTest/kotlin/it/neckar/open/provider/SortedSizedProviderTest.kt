package it.neckar.open.provider.it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.parseInt
import it.neckar.open.provider.MultiProvider
import it.neckar.open.provider.SizedProvider
import it.neckar.open.provider.SortedSizedProvider
import it.neckar.open.provider.sorted
import it.neckar.open.provider.wrapMultiProvider
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
