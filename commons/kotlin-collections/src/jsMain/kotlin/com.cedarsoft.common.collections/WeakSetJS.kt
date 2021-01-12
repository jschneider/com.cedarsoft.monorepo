package com.cedarsoft.common.collections

@JsName("WeakSet")
external class WeakSetJS {
  fun add(v: dynamic)
  fun delete(v: dynamic): Boolean
  fun has(v: dynamic): Boolean
}

/**
 * A weak set implementation for JavaScript
 *
 * Note: only elements of type object are supported!
 */
actual class WeakSet<T> {
  private val backingSet = WeakSetJS()

  actual operator fun contains(element: T): Boolean = backingSet.has(element)

  actual fun add(element: T) {
    backingSet.add(element)
  }

  actual fun remove(element: T): Boolean {
    return backingSet.delete(element)
  }
}
