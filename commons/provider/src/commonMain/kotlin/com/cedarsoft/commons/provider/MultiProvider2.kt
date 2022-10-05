package com.cedarsoft.commons.provider

import com.cedarsoft.charting.annotations.Domain

/**
 * Takes two parameters to provide values
 */
fun interface MultiProvider2<in IndexContext, out T, in P1, in P2> {
  /**
   * Retrieves the value at the given [index].
   */
  fun valueAt(index: Int, param1: P1, param2: P2): T

  /**
   * Retrieves the value at the given [index].
   */
  operator fun get(index: Int, param1: P1, param2: P2): T {
    return valueAt(index, param1, param2)
  }

  companion object {
    /**
     * Creates a new instance of an empty multi provider
     */
    fun <T> empty(): @Domain MultiProvider2<Any, T, Any, Any> {
      return MultiProvider2<Any, T, Any, Any> { _, _, _ -> throw UnsupportedOperationException() }
    }
  }

}
