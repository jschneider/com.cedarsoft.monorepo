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
}

fun DoublesProvider.toList(): List<Double> {
  return List(size()) {
    valueAt(it)
  }
}
