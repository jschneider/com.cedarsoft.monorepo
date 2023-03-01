package it.neckar.open.provider.it.neckar.open.provider

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.parseInt
import it.neckar.open.provider.MultiProvider1
import it.neckar.open.provider.SizedProvider1
import it.neckar.open.provider.SortedSizedProvider1
import it.neckar.open.provider.wrapMultiProvider
import org.junit.jupiter.api.Test

class SortedSizedProvider1Test {
  val unsorted: SizedProvider1<String, Double> = object : SizedProvider1<String, Double> {
    val elements = listOf("17", "22", "5", "9", "1")

    override fun size(param1: Double): Int {
      return elements.size
    }

    override fun valueAt(index: Int, param1: Double): String {
      return elements[index]
    }
  }

  @Test
  fun testSimple() {
    assertThat(unsorted.size(99.9)).isEqualTo(5)

    assertThat(unsorted.toList(99.9)).containsExactly("17", "22", "5", "9", "1")

    val sorted = SortedSizedProvider1(unsorted, compareBy { it.parseInt() })

    assertThat(sorted.size(99.9)).isEqualTo(5)

    assertThat(unsorted.toList(99.9)).containsExactly("17", "22", "5", "9", "1")
    assertThat(sorted.toList(99.9)).containsExactly("1", "5", "9", "17", "22")

    //Mapped indices have now been updated
    assertThat(sorted.mapped2Original(0)).isEqualTo(4) //--> 1
    assertThat(sorted.mapped2Original(4)).isEqualTo(1) //--> 22
  }

  @Test
  fun testUseForMultiProvider() {
    val sorted = SortedSizedProvider1(unsorted, compareBy { it.parseInt() })

    assertThat(unsorted.toList(123.3)).containsExactly("17", "22", "5", "9", "1")
    assertThat(sorted.toList(123.3)).containsExactly("1", "5", "9", "17", "22")

    val multiProvider = MultiProvider1<Int, String, Double> { index, _ -> "value_$index" }
    assertThat(multiProvider.valueAt(0, 123.3)).isEqualTo("value_0")
    assertThat(multiProvider.valueAt(7, 123.3)).isEqualTo("value_7")

    val sortedMultiProvider = sorted.wrapMultiProvider(multiProvider)

    assertThat(sortedMultiProvider.toList(5, 123.3)).containsExactly("value_4", "value_2", "value_3", "value_0", "value_1")
  }
}

fun <T, P1> MultiProvider1<*, T, P1>.toList(size: Int, param1: P1): List<T> {
  return List(size) {
    valueAt(it, param1)
  }
}

/**
 * Collects the values into a list.
 * ATTENTION: Only use for tests!
 */
fun <T, P1> SizedProvider1<T, P1>.toList(param1: P1): List<T> {
  return List(this.size(param1)) {
    valueAt(it, param1)
  }
}
