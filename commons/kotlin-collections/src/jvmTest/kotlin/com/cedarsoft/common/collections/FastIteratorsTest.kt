package com.cedarsoft.common.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class FastIteratorsKtTest {
  @Test
  fun testReverse() {
    val list = DoubleArray(10) { it * 10.0 }

    assertThat(list).hasSize(10)

    var callCount = 0

    list.fastForEachIndexedReverse { index, value ->
      callCount++
      assertThat(index, "for index $index").isEqualTo(list.size - callCount)
      assertThat(value, "for index $index").isEqualTo((list.size - callCount) * 10.0)
    }

    assertThat(callCount).isEqualTo(10)
  }

  @Test
  fun testFastAny() {
    val list = DoubleArrayList()

    //No elements
    assertThat(list.fastFindAny { it > 0.0 }).isFalse()

    list.add(17.0)
    assertThat(list.fastFindAny { it > 0.0 }).isTrue()
    assertThat(list.fastFindAny { it > 17.0 }).isFalse()

    list.add(6.0)
    assertThat(list.fastFindAny { it > 0.0 }).isTrue()
    assertThat(list.fastFindAny { it > 6.0 }).isTrue()
    assertThat(list.fastFindAny { it > 17.0 }).isFalse()
    assertThat(list.fastFindAny { it < 17.0 }).isTrue()
    assertThat(list.fastFindAny { it < 6.0 }).isFalse()
  }

  @Test
  fun testJoin1() {
    var callCount = 0

    1.join({ fail("Where is the exception?") }) {
      callCount++
    }
    assertThat(callCount).isEqualTo(1)
  }

  @Test
  fun testJoin2() {
    var callCount = 0
    var between = 0

    2.join({ between++ }) {
      callCount++
    }
    assertThat(callCount).isEqualTo(2)
    assertThat(between).isEqualTo(1)
  }

  @Test
  fun testIntIt() {
    var expected = 0

    7.fastFor {
      assertThat(it).isEqualTo(expected)
      expected++
    }

    assertThat(expected).isEqualTo(7)
  }

  @Test
  fun testReduce() {
    listOf(2, 3, 4).reduce { first, second ->
      first + second
    }.let {
      assertThat(it).isEqualTo(2 + 3 + 4)
    }

    listOf(2, 3, 4).fastReduce { first, second ->
      first + second
    }.let {
      assertThat(it).isEqualTo(2 + 3 + 4)
    }
  }

  @Test
  fun testFastMapNonNull() {
    listOf("1", "2", "3", "asdf", "4").fastMapNotNull {
      it.toIntOrNull()
    }.let {
      assertThat(it).hasSize(4)
      assertThat(it).containsOnly(1, 2, 3, 4)
    }

    listOf<String>().fastMapNotNull {
      throw UnsupportedOperationException()
    }.let {
      assertThat(it).hasSize(0)
    }
  }

  @Test
  fun testFastMap() {
    listOf("1", "2", "3", "4").fastMapNotNull {
      it.toInt()
    }.let {
      assertThat(it).hasSize(4)
      assertThat(it).containsOnly(1, 2, 3, 4)
    }

    listOf<String>().fastMapNotNull {
      throw UnsupportedOperationException()
    }.let {
      assertThat(it).hasSize(0)
    }
  }

  @Test
  fun testFastForeachReversed() {
    val array = Array(7) {
      it
    }

    assertThat(array).hasSize(7)
    assertThat(array.last()).isEqualTo(6)


    var count = 0
    array.fastForEachReversed {
      assertThat(it, "value $it").isEqualTo(6 - count)
      count++
    }

    assertThat(count).isEqualTo(7)
  }

  @Test
  fun testFastForEach() {
    val array = IntArray(7) {
      it
    }

    var counter = 0
    array.forEachIndexed { index, value ->
      assertThat(index)
        .isEqualTo(counter)

      assertThat(index)
        .isEqualTo(value)

      counter++
    }


  }
}
