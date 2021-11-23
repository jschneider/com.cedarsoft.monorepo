package com.cedarsoft.formatting

import com.cedarsoft.common.collections.cache
import com.cedarsoft.i18n.I18nConfiguration

/**
 * A formatter that returns cached values.
 *
 * This is a tagging interface to ensure a cached formatter is used
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
interface CachedFormatter : NumberFormat {
  /**
   * Returns the current cache size
   */
  val currentCacheSize: Int
}

/**
 * A formatter that caches the results
 */
class DefaultCachedFormatter internal constructor(
  val formatter: NumberFormat,
  /**
   * The maximum size of the cache
   */
  val cacheSize: Int = 500,

  /**
   * The hash function that is used to calculate the hash for the current value.
   * This method can *also* use external variables (e.g. a locale or another configuration).
   */
  val hashFunction: (value: Double, i18nConfiguration: I18nConfiguration) -> Int = defaultHashFunction

) : CachedFormatter {

  init {
    require(formatter !is CachedFormatter) { "cannot cache an already cached number format" }
  }

  /**
   * The cache for the "normal" formatted strings
   */
  private val formatCache = cache<Int, String>("DefaultCachedFormatter", cacheSize)

  /**
   * Returns the size of the cache
   */
  override val currentCacheSize: Int
    get() = formatCache.size

  override fun format(value: Double, i18nConfiguration: I18nConfiguration): String {
    //Calculate the hash code to avoid instantiation of objects
    val key = hashFunction(value, i18nConfiguration)

    return formatCache.getOrStore(key) {
      formatter.format(value, i18nConfiguration)
    }
  }

  override val precision: Double
    get() = formatter.precision

  companion object {
    /**
     * The hash function that is used per default
     */
    val defaultHashFunction: (value: Double, i18nConfiguration: I18nConfiguration) -> Int = { value, i18nConfiguration ->
      31 * value.hashCode() + i18nConfiguration.hashCode()
    }
  }
}

/**
 * Caches the results of the number format
 */
fun NumberFormat.cached(
  cacheSize: Int = 500,
  /**
   * Calculates an additional hash that will be added to the hash of the value.
   * Can be used to create a unique hash for other (external) factors: For example a unit
   */
  additionalHashFunction: (() -> Int)? = null
): CachedFormatter {
  //Create a custom hash function - only if a additionalHashFunction is provided
  val hashFunction = additionalHashFunction?.let {
    //Create a custom hash function
    { value, i18nConfiguration ->
      DefaultCachedFormatter.defaultHashFunction(value, i18nConfiguration) + additionalHashFunction()
    }
  } ?: DefaultCachedFormatter.defaultHashFunction //Fallback to the default hash function

  return DefaultCachedFormatter(this, cacheSize = cacheSize, hashFunction = hashFunction)
}

/**
 * Helper method to avoid unnecessary calls to cached
 */
@Deprecated("Do not cache a cached formatter", ReplaceWith("this"), level = DeprecationLevel.ERROR)
fun CachedFormatter.cached(
  cacheSize: Int = 500,
  hashFunction: (value: Double, i18nConfiguration: I18nConfiguration) -> Int = { _, _ -> throw UnsupportedOperationException("must not be called!") }
): CachedFormatter {
  return this
}

