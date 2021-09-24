package com.cedarsoft.formatting

import com.cedarsoft.common.collections.cache
import com.cedarsoft.i18n.I18nConfiguration
import kotlin.jvm.JvmOverloads

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
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class DefaultCachedFormatter @JvmOverloads constructor(
  val formatter: NumberFormat,
  /**
   * The maximum size of the cache
   */
  val cacheSize: Int = 500
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
    val key = 31 * value.hashCode() + i18nConfiguration.hashCode()

    return formatCache.getOrStore(key) {
      formatter.format(value, i18nConfiguration)
    }
  }

  override val precision: Double
    get() = formatter.precision
}

/**
 * Caches the results of the number format
 */
fun NumberFormat.cached(cacheSize: Int = 500): CachedFormatter {
  return DefaultCachedFormatter(this, cacheSize = cacheSize)
}

/**
 * Helper method to avoid unnecessary calls to cached
 */
@Deprecated("Do not cache a cached formatter", ReplaceWith("this"), level = DeprecationLevel.ERROR)
fun CachedFormatter.cached(cacheSize: Int = 500): CachedFormatter {
  return this
}

