package com.cedarsoft.formatting

import com.cedarsoft.common.collections.cache

/**
 * A cache for the percentage formats
 */
object PercentageFormatsCache {
  /**
   * Cache for decimal formats
   */
  private val cache = cache<Int, CachedNumberFormat>("PercentageFormatsCache", 50)

  /**
   * Returns a (cached) decimal format with a fixed number of decimals
   */
  fun get(numberOfDecimals: Int): CachedNumberFormat {
    return get(numberOfDecimals, numberOfDecimals)
  }

  fun get(numberOfDecimals: Int, useGrouping: Boolean = true): CachedNumberFormat {
    return get(numberOfDecimals, numberOfDecimals, 1, useGrouping)
  }

  fun get(
    /**
     * The maximum fraction digits
     */
    maximumFractionDigits: Int = 2,
    /**
     * The minimum fraction digits
     */
    minimumFractionDigits: Int = 0,
    /**
     * The minimum integer digits for the format
     */
    minimumIntegerDigits: Int = 1, // must be greater than 0 in JavaScript
    /**
     * Whether to use grouping or not
     */
    useGrouping: Boolean = true
  ): CachedNumberFormat {
    val hashCode = calculateHashCode(maximumFractionDigits, minimumFractionDigits, minimumIntegerDigits, useGrouping)

    return cache.getOrStore(hashCode) {
      val decimalFormat = DecimalFormatsCache.get(maximumFractionDigits, minimumFractionDigits, minimumIntegerDigits, useGrouping)
      PercentageFormat(decimalFormat).cached()
    }
  }

  /**
   * Calculates the hash code for the given values
   */
  private fun calculateHashCode(
    maximumFractionDigits: Int,
    minimumFractionDigits: Int,
    minimumIntegerDigits: Int,
    useGrouping: Boolean
  ): Int {
    var result = maximumFractionDigits
    result = 31 * result + minimumFractionDigits
    result = 31 * result + minimumIntegerDigits
    result = 31 * result + useGrouping.hashCode()
    return result
  }

}
