package com.cedarsoft.commons.provider

/**
 * Sized provider that takes one parameter
 */
interface SizedProvider1<out T, in P1> : MultiProvider1<T, P1>, HasSize1<P1> {
  /**
   * Returns the first element
   * Throws a [NoSuchElementException] if there are no elements
   */
  fun first(param1: P1): T {
    if (size(param1) == 0) {
      throw NoSuchElementException("Size is 0")
    }
    return this.valueAt(0, param1)
  }

  /**
   * Returns the last element.
   * Throws a [NoSuchElementException] if there are no elements
   */
  fun last(param1: P1): T {
    val size = size(param1)
    if (size == 0) {
      throw NoSuchElementException("Size is 0")
    }
    return this.valueAt(size - 1, param1)
  }

  /**
   * Returns the element at the given index or null if the index is >= [size]
   */
  fun getOrNull(index: Int, param1: P1): T? {
    if (index >= size(param1)) {
      return null
    }

    return valueAt(index, param1)
  }
}
