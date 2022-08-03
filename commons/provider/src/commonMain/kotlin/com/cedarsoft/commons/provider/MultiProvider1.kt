package com.cedarsoft.commons.provider

/**
 * Takes one parameter to provide values
 */
interface MultiProvider1<in IndexContext, out T, in P1> : HasSize1<P1> {
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
}
