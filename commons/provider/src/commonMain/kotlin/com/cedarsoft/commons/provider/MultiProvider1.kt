package com.cedarsoft.commons.provider

import com.cedarsoft.charting.annotations.Domain
import kotlin.reflect.KProperty0

/**
 * Takes one parameter to provide values
 */
fun interface MultiProvider1<in IndexContext, out T, in P1> {
  /**
   * Retrieves the value at the given [index].
   */
  fun valueAt(index: Int, param1: P1): T

  /**
   * Retrieves the value at the given [index].
   */
  operator fun get(index: Int, param1: P1): T {
    return valueAt(index, param1)
  }

  companion object {
    /**
     * Creates a new instance of an empty multi provider
     */
    fun <T> empty(): @Domain MultiProvider1<Any, T, Any> {
      return MultiProvider1<Any, T, Any> { _, _ -> throw UnsupportedOperationException() }
    }

    /**
     * Always returns the provided instance
     */
    fun <IndexContext, T, P1> always(value: T): MultiProvider1<IndexContext, T, P1> {
      return MultiProvider1 { _, _ -> value }
    }
  }
}

/**
 * Returns a [MultiProvider1] that delegates all calls to the current value of this property
 */
fun <IndexContext, T, P1> KProperty0<MultiProvider1<IndexContext, T, P1>>.delegate(): MultiProvider1<IndexContext, T, P1> {
  return MultiProvider1 { index, param1 -> this@delegate.get().valueAt(index, param1) }
}
