package com.cedarsoft.commons.provider

/**
 * Provides boolean values
 */
interface BooleanValuesProvider : HasSize {
  /**
   * Retrieves the value at the given [index].
   *
   * @param index a value between 0 (inclusive) and [size] (exclusive)
   */
  fun valueAt(index: Int): Boolean
}
