package com.cedarsoft.commons.provider

/**
 * Maps one index to another
 */
fun interface IndexMapping {
  /**
   * Provides the mapped index for the provided index
   */
  fun mappedIndex(originalIndex: Int): Int
}
