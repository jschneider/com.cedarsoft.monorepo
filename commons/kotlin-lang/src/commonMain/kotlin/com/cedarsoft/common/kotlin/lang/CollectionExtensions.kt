package com.cedarsoft.common.kotlin.lang

import com.cedarsoft.charting.annotations.Domain
import com.cedarsoft.unit.other.pct

/**
 *
 */


inline fun count(cond: (index: Int) -> Boolean): Int {
  var counter = 0
  while (cond(counter)) counter++
  return counter
}

inline fun <reified T> mapWhile(cond: (index: Int) -> Boolean, gen: (Int) -> T): List<T> = arrayListOf<T>().apply { while (cond(this.size)) this += gen(this.size) }
inline fun <reified T> mapWhileArray(cond: (index: Int) -> Boolean, gen: (Int) -> T): Array<T> = mapWhile(cond, gen).toTypedArray()

fun <T> List<T>.getCyclic(index: Int) = this[index umod this.size]
fun <T> Array<T>.getCyclic(index: Int) = this[index umod this.size]


/**
 * Returns the n th element from the list. Uses modulo if the index is larger than the size of the list
 */
fun <T> List<T>.getModulo(index: Int): T {
  //This calculation produces a "wrap around" effect for negative indices
  return this[index.wrapAround(size)]
}

/**
 * Returns the n th element from the list. Uses modulo if the index is larger than the size of the list.
 * Returns null if the list is empty.
 */
fun <T> List<T>.getModuloOrNull(index: Int): T? {
  //This calculation produces a "wrap around" effect for negative indices
  return if (isEmpty()) null else this[index.wrapAround(size)]
}

/**
 * Iterates over this collection delivering its elements to [consumer] while the [consumer] returns true for a consumed element
 * @see [consumeUntil]
 */
fun <E> Collection<E>.consumeWhile(consumer: (E) -> Boolean) {
  val iter = iterator()
  while (iter.hasNext()) {
    if (!consumer(iter.next())) {
      return
    }
  }
}

/**
 * Iterates over this collection delivering its elements to [consumer] until the [consumer] returns false for a consumed element
 * @see [consumeWhile]
 */
fun <E> Collection<E>.consumeUntil(consumer: (E) -> Boolean) {
  val iter = iterator()
  while (iter.hasNext()) {
    if (consumer(iter.next())) {
      return
    }
  }
}

/**
 * Returns a list of values that have relative values (the sum of all values is equal to 1.0).
 */
fun List<@Domain Double>.toRelativeValues(): List<@pct Double> {
  @Domain val sum = this.sum()
  return map {
    1 / sum * it
  }
}

/**
 * Removes elements from the list until the max size has been reached
 */
fun <T> MutableList<T>.deleteFromStartUntilMaxSize(maxSize: Int) {
  require(maxSize >= 0) { "Invalid max size: $maxSize" }

  while (this.size > maxSize) {
    removeAt(0)
  }
}

/**
 * Sets  the element as last element. Replaces the last element!
 */
fun <E> MutableList<E>.setLast(element: E) {
  this[lastIndex] = element
}
