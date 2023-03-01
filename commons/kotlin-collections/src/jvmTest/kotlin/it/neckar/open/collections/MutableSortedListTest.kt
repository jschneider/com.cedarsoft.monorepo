package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class MutableSortedListTest {

  @Test
  fun getSize() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.size).isEqualTo(0)
    list.add(4711)
    assertThat(list.size).isEqualTo(1)
  }

  @Test
  fun contains() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.contains(4711)).isFalse()
    list.add(4711)
    assertThat(list.contains(4711)).isTrue()
  }

  @Test
  fun containsAll() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.containsAll(listOf(1, 2, 3))).isFalse()
    list.add(1)
    assertThat(list.containsAll(listOf(1, 2, 3))).isFalse()
    list.add(2)
    assertThat(list.containsAll(listOf(1, 2, 3))).isFalse()
    list.add(3)
    assertThat(list.containsAll(listOf(1, 2, 3))).isTrue()
  }

  @Test
  fun get() {
    val list = mutableSortedListOf<Int>()
    try {
      list[0]
      assertFails("index out of bounds") {}
    } catch (e: Exception) {
    }
    list.add(4711)
    assertThat(list[0]).isEqualTo(4711)
  }

  @Test
  fun indexOf() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.indexOf(4711)).isEqualTo(-1)
    list.add(4711)
    assertThat(list.indexOf(4711)).isEqualTo(0)
    list.add(4711)
    assertThat(list.indexOf(4711)).isIn(0, 1)
    list.add(85)
    assertThat(list.indexOf(85)).isEqualTo(0)
    assertThat(list.indexOf(4711)).isIn(1, 2)
  }

  @Test
  fun isEmpty() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.isEmpty()).isTrue()
    list.add(4711)
    assertThat(list.isEmpty()).isFalse()
    list.clear()
    assertThat(list.isEmpty()).isTrue()
  }

  @Test
  operator fun iterator() {
    val list = mutableSortedListOf<Int>()
    val iterator = list.iterator()
    assertThat(iterator.hasNext()).isFalse()
  }

  @Test
  fun lastIndexOf() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.lastIndexOf(4711)).isEqualTo(-1)
    list.add(4711)
    assertThat(list.lastIndexOf(4711)).isEqualTo(0)
    list.add(4711)
    assertThat(list.lastIndexOf(4711)).isEqualTo(1)
    list.add(85)
    assertThat(list.lastIndexOf(4711)).isIn(2)
  }

  @Test
  fun firstIndexOf() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.firstIndexOf(4711)).isEqualTo(-1)
    list.add(4711)
    assertThat(list.firstIndexOf(4711)).isEqualTo(0)
    list.add(4711)
    assertThat(list.firstIndexOf(4711)).isEqualTo(0)
    list.add(85)
    assertThat(list.firstIndexOf(4711)).isIn(1)
  }

  @Test
  fun testAdd() {
    val list = mutableSortedListOf<Int>()
    list.add(1)
    list.add(3)
    list.add(2)
    assertThat(list[0]).isEqualTo(1)
    assertThat(list[1]).isEqualTo(2)
    assertThat(list[2]).isEqualTo(3)
  }

  @Test
  fun testAddWithIndex() {
    val list = mutableSortedListOf<Int>()
    list.add(0, 1)
    assertThat(list[0]).isEqualTo(1)
    list.add(1, 2)
    assertThat(list[1]).isEqualTo(2)

    try {
      list.add(0, 8)
      assertFails("list no longer sorted") {}
    } catch (e: Exception) {
    }

    try {
      list.add(-1, 5)
      assertFails("negative index") {}
    } catch (e: Exception) {
    }

  }

  @Test
  fun testAddAll() {
    val list = mutableSortedListOf<Int>()
    list.addAll(listOf(11, 7, 23, 15))
    assertThat(list.isEmpty()).isFalse()
    assertThat(list).hasSize(4)
    assertThat(list[0]).isEqualTo(7)
    assertThat(list[1]).isEqualTo(11)
    assertThat(list[2]).isEqualTo(15)
    assertThat(list[3]).isEqualTo(23)
  }

  @Test
  fun testAddAllWithIndex() {
    val list = mutableSortedListOf<Int>()
    list.addAll(0, listOf(11, 7, 23, 15))
    assertThat(list.isEmpty()).isFalse()
    assertThat(list).hasSize(4)
    assertThat(list[0]).isEqualTo(7)
    assertThat(list[1]).isEqualTo(11)
    assertThat(list[2]).isEqualTo(15)
    assertThat(list[3]).isEqualTo(23)

    list.addAll(2, listOf(12, 13, 14))
    assertThat(list).hasSize(7)
    assertThat(list[2]).isEqualTo(12)
    assertThat(list[3]).isEqualTo(13)
    assertThat(list[4]).isEqualTo(14)

    try {
      list.addAll(0, listOf(4711, 4712))
      assertFails("list no longer sorted") {}
    } catch (e: Exception) {
    }
  }

  @Test
  fun clear() {
    val list = mutableSortedListOf<Int>()
    list.add(4711)
    assertThat(list).hasSize(1)
    list.clear()
    assertThat(list.isEmpty()).isTrue()
  }

  @Test
  fun testListIterator() {
    val list = mutableSortedListOf<Int>()

    run {
      list.addAll(listOf(1, 2, 3))
      val iterator = list.listIterator(list.size)
      while (iterator.hasPrevious()) {
        iterator.previous()
        iterator.remove()
      }
      assertThat(list.isEmpty())
    }

    run {
      val iterator = list.listIterator()
      for (i in 1..5) {
        iterator.add(i)
      }
      assertThat(list).hasSize(5)
      assertListsHaveSameElementsInSameOrder(list, listOf(1, 2, 3, 4, 5))
    }
  }

  @Test
  fun testListIteratorAt() {
    val elements = listOf(1, 2, 3)
    val list = mutableSortedListOf<Int>()
    list.addAll(elements)

    run {
      val iterator = list.listIterator(0)
      var index = 0
      while (iterator.hasNext()) {
        val nextValue = iterator.next()
        assertThat(nextValue).isEqualTo(elements[index])
        ++index
      }
      assertThat(index).isEqualTo(list.size)
    }

    run {
      val iterator = list.listIterator(list.size)
      var index = list.size
      while (iterator.hasPrevious()) {
        val previousValue = iterator.previous()
        assertThat(previousValue).isEqualTo(elements[index - 1])
        --index
      }
      assertThat(index).isEqualTo(0)
    }
  }

  @Test
  fun remove() {
    val list = mutableSortedListOf<Int>()
    assertThat(list.remove(4711)).isFalse()
    list.add(4711)
    assertThat(list.remove(4711)).isTrue()
  }

  @Test
  fun removeAll() {
    val list = mutableSortedListOf<Int>()

    assertThat(list.removeAll(listOf(1, 2))).isFalse()
    list.addAll(listOf(1, 2, 3))
    assertThat(list.removeAll(listOf(1, 2))).isTrue()
    assertThat(list).hasSize(1)
    assertThat(list[0]).isEqualTo(3)

    list.clear()
    list.addAll(listOf(1, 2, 3))
    assertThat(list.removeAll { it > 3 }).isFalse()
    assertThat(list).hasSize(3)

    assertThat(list.removeAll { it > 1 }).isTrue()
    assertThat(list).hasSize(1)
    assertThat(list[0]).isEqualTo(1)
  }

  @Test
  fun removeAt() {
    val list = mutableSortedListOf<Int>()

    try {
      list.removeAt(0)
      assertFails("index out of bounds") {}
    } catch (e: Exception) {
    }

    list.add(1)
    assertThat(list.removeAt(0)).isEqualTo(1)
    assertThat(list.isEmpty()).isTrue()
  }

  @Test
  fun retainAll() {
    val list = mutableSortedListOf<Int>()

    assertThat(list.retainAll(listOf(1, 2, 3))).isFalse()
    list.add(1)
    assertThat(list.retainAll(listOf(1, 2, 3))).isFalse()
    assertThat(list).hasSize(1)
    assertThat(list[0]).isEqualTo(1)

    list.addAll(listOf(2, 3, 4, 5))
    assertThat(list).hasSize(5)
    assertThat(list.retainAll(listOf(1, 2, 3))).isTrue()
    assertThat(list).hasSize(3)
    assertListsHaveSameElementsInSameOrder(list, listOf(1, 2, 3))

    list.clear()
    list.addAll(listOf(1, 2, 3))
    assertThat(list).hasSize(3)
    assertThat(list.retainAll { it > 0 }).isFalse()
    assertThat(list.retainAll { it > 2 }).isTrue()
    assertListsHaveSameElementsInSameOrder(list, listOf(3))
  }

  @Test
  fun set() {
    val list = mutableSortedListOf<Int>()
    list.addAll(listOf(17, 23, 37, 51))

    try {
      list[-1] = 3
      assertFails("index is negative") {}
    } catch (e: Exception) {
    }

    try {
      list[4] = 4711
      assertFails("index is too large") {}
    } catch (e: Exception) {
    }

    try {
      list[0] = 24
      assertFails("value is too high") {}
    } catch (e: Exception) {
    }

    try {
      list[1] = 38
      assertFails("value is too high") {}
    } catch (e: Exception) {
    }

    try {
      list[3] = 36
      assertFails("value is too low") {}
    } catch (e: Exception) {
    }

    try {
      list[2] = 22
      assertFails("value is too low") {}
    } catch (e: Exception) {
    }


    assertThat(list.set(0, 15)).isEqualTo(17)
    assertThat(list.set(1, 25)).isEqualTo(23)
    assertThat(list.set(2, 33)).isEqualTo(37)
    assertThat(list.set(3, 111)).isEqualTo(51)

    assertListsHaveSameElementsInSameOrder(list, listOf(15, 25, 33, 111))
  }

  @Disabled //sub list is not yet implemented
  @Test
  fun subList() {
    val list = mutableSortedListOf<String>()

    try {
      list.subList(0, 3)
      assertFails("out of bounds") {}
    } catch (e: Exception) {
    }

    list.addAll(listOf("a", "b", "c"))
    assertThat(list.subList(0, 0)).isEmpty()
    assertListsHaveSameElementsInSameOrder(list.subList(0, 1), listOf("a"))
    assertListsHaveSameElementsInSameOrder(list.subList(0, 2), listOf("a", "b"))
    assertListsHaveSameElementsInSameOrder(list.subList(0, 3), listOf("a", "b", "c"))
    assertListsHaveSameElementsInSameOrder(list.subList(0, 4), listOf("a", "b", "c"))
    assertListsHaveSameElementsInSameOrder(list.subList(1, 3), listOf("b", "c"))

    val subList = list.subList(0, 2)
    assertListsHaveSameElementsInSameOrder(subList, listOf("a", "b"))

    subList.remove("a")
    assertListsHaveSameElementsInSameOrder(subList, listOf("b"))
    assertListsHaveSameElementsInSameOrder(list, listOf("b", "c"))

    subList.add("d")
    assertListsHaveSameElementsInSameOrder(subList, listOf("b", "d"))
    assertListsHaveSameElementsInSameOrder(list, listOf("b", "c", "d"))

    subList.removeAt(1)
    assertListsHaveSameElementsInSameOrder(subList, listOf("d"))
    assertListsHaveSameElementsInSameOrder(list, listOf("c", "d"))
  }

  @Test
  fun getComparator() {
    val list = mutableSortedListOf<String>()
    assertThat(list.comparator).isNotNull()

    val list2 = mutableSortedListOf<String>(reverseOrder())
    assertThat(list.comparator).isNotNull()
    list2.addAll(listOf("a", "b", "c"))
    assertThat(list2).hasSize(3)
    assertListsHaveSameElementsInSameOrder(list2, listOf("c", "b", "a"))
  }

  private fun assertListsHaveSameElementsInSameOrder(list1: List<*>, list2: List<*>) {
    assertThat(list1.size).isEqualTo(list2.size)
    for (i in 0 until list1.size) {
      assertThat(list1[i]).isEqualTo(list2[i])
    }
  }
}
