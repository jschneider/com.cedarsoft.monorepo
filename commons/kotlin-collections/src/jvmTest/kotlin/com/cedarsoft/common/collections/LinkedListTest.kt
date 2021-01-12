package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LinkedListTest {

  @Test
  fun testIt() {

    val list = LinkedList<String>()
    assertEquals(0, list.getLength())

    list.addLast("last")
    assertEquals(1, list.getLength())

    list.clear()
    assertEquals(0, list.getLength())

    list.addFirst("first")
    list.addLast("last")
    assertEquals(2, list.getLength())

    list.clear()

    for (i in 1..100) {
      list.addLast("entry$i")
      assertEquals(i, list.getLength())
    }

    for (i in 1..95) {
      assertEquals("entry$i", list.pollFirst())
    }
    assertEquals(5, list.getLength())

    list.clear()

    for (i in 1..10) {
      list.addLast("entry$i")
    }

    for (i in 10 downTo 3) {
      assertEquals("entry$i", list.pollLast())
    }
    assertEquals(2, list.getLength())
  }


  @Test
  internal fun testIt2() {
    val list = LinkedList<String>()
    assertThat(list.first).isNull()
    assertThat(list.firstValue).isNull()
    assertThat(list.last).isNull()
    assertThat(list.lastValue).isNull()

    list.addFirst("a")
    assertThat(list.first).isNotNull()
    assertThat(list.firstValue).isEqualTo("a")
    assertThat(list.last).isNotNull()
    assertThat(list.lastValue).isEqualTo("a")
  }
}
