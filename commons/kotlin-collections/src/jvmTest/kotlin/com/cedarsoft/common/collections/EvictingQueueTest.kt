package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class EvictingQueueTest {
  @Test
  fun testIt() {
    val queue = EvictingQueue<Int>(2)

    assertThat(queue).hasSize(0)
    queue.add(77)
    assertThat(queue).hasSize(1)
    assertThat(queue).containsOnly(77)

    queue.add(78)
    assertThat(queue).hasSize(2)
    assertThat(queue).containsOnly(77, 78)

    queue.add(79)
    assertThat(queue).hasSize(2)
    assertThat(queue).containsOnly(78, 79)
  }

  @Test
  internal fun testAddAll() {
    val queue = EvictingQueue<Int>(3)

    assertThat(queue).hasSize(0)
    queue.add(5)
    queue.add(6)
    queue.add(7)
    assertThat(queue).containsOnly(5, 6, 7)

    //Empty
    queue.addAll(listOf())
    assertThat(queue).containsOnly(5, 6, 7)

    //1 element
    queue.addAll(listOf(11))
    assertThat(queue).containsOnly(6, 7, 11)

    //2 elements
    queue.addAll(listOf(21, 22))
    assertThat(queue).containsOnly(11, 21, 22)

    //3 elements
    queue.addAll(listOf(31, 32, 33))
    assertThat(queue).containsOnly(31, 32, 33)

    //4 elements
    queue.addAll(listOf(41, 42, 43, 44))
    assertThat(queue).containsOnly(42, 43, 44)
  }
}
