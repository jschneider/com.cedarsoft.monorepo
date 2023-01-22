package com.cedarsoft.commons.provider

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.kotlin.lang.DoublesComparator
import org.junit.jupiter.api.Test

class SortedDoublesProviderTest {
  @Test
  fun testIt() {
    val unsorted = DoublesProvider.forDoubles(10.0, 7.0, 5.0, 11.0, 1.0)
    val sorted = unsorted.sorted()

    assertThat(unsorted.toList()).containsExactly(10.0, 7.0, 5.0, 11.0, 1.0)

    //Call to size triggers the sorting
    assertThat(sorted.size()).isEqualTo(5)
    assertThat(sorted.toList()).containsExactly(1.0, 5.0, 7.0, 10.0, 11.0)

    assertThat(sorted.mapped2Original(0)).isEqualTo(4)
    assertThat(sorted.mapped2Original(1)).isEqualTo(2)
    assertThat(sorted.mapped2Original(2)).isEqualTo(1)
    assertThat(sorted.mapped2Original(3)).isEqualTo(0)
    assertThat(sorted.mapped2Original(4)).isEqualTo(3)
  }

  @Test
  fun testCustomComparatorReversed() {
    val unsorted = DoublesProvider.forDoubles(10.0, 7.0, 5.0, 11.0, 1.0)
    val sorted = unsorted.sorted(object : DoublesComparator {
      override fun compare(valueA: Double, valueB: Double): Int {
        return valueB.compareTo(valueA)
      }
    })

    assertThat(unsorted.toList()).containsExactly(10.0, 7.0, 5.0, 11.0, 1.0)

    //Call to size triggers the sorting
    assertThat(sorted.size()).isEqualTo(5)
    assertThat(sorted.toList()).containsExactly(11.0, 10.0, 7.0, 5.0, 1.0)
  }

  private annotation class MyIndex1
  private annotation class MyIndex2

  @Test
  fun testWithMultiProvider() {
    val unsorted = DoublesProvider.forDoubles(10.0, 7.0, 5.0, 11.0, 1.0)
    val sorted = unsorted.sorted()

    val multiProvider = MultiProvider.forListModulo<MyIndex1, String>(listOf("10.0", "7.0", "5.0", "11.0", "1.0"))
    val sortedMultiProvider = sorted.wrapMultiProvider<MyIndex1, MyIndex2, String>(multiProvider)

    //Indices must match!
    assertThat(unsorted[0]).isEqualTo(10.0)
    assertThat(multiProvider.valueAt(0)).isEqualTo("10.0")

    //Trigger recalculation
    assertThat(sorted.size()).isEqualTo(5)

    assertThat(sorted[0]).isEqualTo(1.0)
    assertThat(sortedMultiProvider.valueAt(0)).isEqualTo("1.0")

    assertThat(sorted[1]).isEqualTo(5.0)
    assertThat(sortedMultiProvider.valueAt(1)).isEqualTo("5.0")

    assertThat(sorted[4]).isEqualTo(11.0)
    assertThat(sortedMultiProvider.valueAt(4)).isEqualTo("11.0")
  }

  @Test
  fun testSimpleMappingSample() {
    val unsorted = DoublesProvider.forDoubles(20.0, 30.0, 40.0, 10.0)
    val sorted = unsorted.sorted()

    //Trigger recalculation
    sorted.size()
    assertThat(sorted.toList()).isEqualTo(listOf(10.0, 20.0, 30.0, 40.0))

    assertThat(unsorted[0]).isEqualTo(20.0)
    assertThat(sorted[0]).isEqualTo(10.0)

    assertThat(unsorted[1]).isEqualTo(30.0)
    assertThat(sorted[1]).isEqualTo(20.0)

    assertThat(unsorted[2]).isEqualTo(40.0)
    assertThat(sorted[2]).isEqualTo(30.0)

    assertThat(unsorted[3]).isEqualTo(10.0)
    assertThat(sorted[3]).isEqualTo(40.0)

    //Verify index
    assertThat(sorted.mapped2Original(0)).isEqualTo(3)
    assertThat(sorted.mapped2Original(1)).isEqualTo(0)
    assertThat(sorted.mapped2Original(2)).isEqualTo(1)
    assertThat(sorted.mapped2Original(3)).isEqualTo(2)
  }
}

fun DoublesProvider.toList(): List<Double> {
  return List(size()) {
    valueAt(it)
  }
}
