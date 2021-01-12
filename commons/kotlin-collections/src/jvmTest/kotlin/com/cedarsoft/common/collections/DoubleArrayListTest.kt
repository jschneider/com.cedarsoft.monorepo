package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class DoubleArrayListTest {
  @Test
  fun testAdd() {
    val list = DoubleArrayList(0)
    assertThat(list.size).isEqualTo(0)
    try {
      list[0]
      fail("Where is the exception?")
    } catch (e: IndexOutOfBoundsException) {
    }

    list[0] = 17.0
    assertThat(list[0]).isEqualTo(17.0)
    assertThat(list.size).isEqualTo(1)
  }

  @Test
  fun testSortInline() {
    val list = DoubleArrayList(5)
    list.add(1.0)
    list.add(3.0)
    list.add(2.0)

    assertThat(list.size).isEqualTo(3)
    assertThat(list.contains(1.0)).isTrue()
    assertThat(list.contains(3.0)).isTrue()
    assertThat(list.contains(2.0)).isTrue()

    val sorted = list.sort()

    assertThat(sorted).isSameAs(list)
    assertThat(sorted.size).isEqualTo(3)

    assertThat(sorted.indexOf(1.0)).isEqualTo(0)
    assertThat(sorted.indexOf(2.0)).isEqualTo(1)
    assertThat(sorted.indexOf(3.0)).isEqualTo(2)

  }
}
