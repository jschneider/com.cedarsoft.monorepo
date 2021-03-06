package com.cedarsoft.common.collections

import java.util.WeakHashMap

/**
 * A weak set, meaning references to elements are held weakly.
 */
actual class WeakSet<T> {
  private val backingMap: WeakHashMap<T, Boolean> = WeakHashMap()

  actual operator fun contains(element: T): Boolean = backingMap.containsKey(element)

  actual fun add(element: T) {
    backingMap[element] = commonValue
  }

  actual fun remove(element: T): Boolean {
    return backingMap.remove(element, commonValue)
  }

  fun isEmpty(): Boolean {
    return backingMap.isEmpty()
  }

  val size: Int
    get() {
      return backingMap.size
    }

  companion object {
    private const val commonValue: Boolean = true
  }
}
