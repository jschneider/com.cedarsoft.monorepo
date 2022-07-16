package com.cedarsoft.commons.provider

/**
 * Takes two parameters to provide values
 */
interface MultiProvider2<out T, in P1, in P2> {
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
}
