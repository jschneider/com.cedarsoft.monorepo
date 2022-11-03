package com.cedarsoft.common.kotlin.lang

/**
 * Maps a single double to an object
 */
fun interface DoubleMapFunction<T> {
  /**
   * Provides the value for a double
   */
  operator fun invoke(value: Double): T
}

/**
 * Maps a double value to a double value
 */
fun interface Double2Double {
  /**
   * Provides the value for a provided double
   */
  operator fun invoke(value: Double): Double
}


/**
 * Compares two doubles
 */
fun interface DoublesComparator {
  /**
   * See [Comparator.compare]
   */
  fun compare(valueA: Double, valueB: Double): Int

  companion object {
    /**
     * Sorts by natural order
     */
    val natural: DoublesComparator = DoublesComparator { valueA, valueB ->
      valueA.compareTo(valueB)
    }

    val naturalReversed: DoublesComparator = DoublesComparator { valueA, valueB ->
      valueB.compareTo(valueA)
    }
  }
}
