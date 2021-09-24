package com.cedarsoft.formatting

import com.cedarsoft.common.collections.cache
import com.cedarsoft.i18n.I18nConfiguration
import kotlin.jvm.JvmOverloads

/**
 * A formatter that returns cached values.
 *
 * This interface should be used at declarations (e.g. in Styles) to ensure a cache is used
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
interface CachedDateTimeFormatter : DateTimeFormat {
  /**
   * Returns the current cache size
   */
  val currentCacheSize: Int
}

/**
 * A formatter that caches the results
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class DefaultCachedDateTimeFormatter @JvmOverloads constructor(
  val formatter: DateTimeFormat,
  /**
   * The maximum size of the cache
   */
  val cacheSize: Int = 500
) : CachedDateTimeFormatter {

  init {
    require(formatter !is CachedFormatter) { "cannot cache an already cached dateTime format" }
  }

  /**
   * The cache for the "normal" formatted strings
   */
  private val formatCache = cache<Int, String>("DefaultCachedDateTimeFormatter", cacheSize)

  /**
   * Returns the size of the cache
   */
  override val currentCacheSize: Int
    get() = formatCache.size

  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val key = 31 * timestamp.hashCode() + 17 * i18nConfiguration.hashCode()

    return formatCache.getOrStore(key) {
      formatter.format(timestamp, i18nConfiguration)
    }
  }
}

/**
 * Caches the results of the dateTime format
 */
fun DateTimeFormat.cached(cacheSize: Int = 100): CachedDateTimeFormatter {
  return DefaultCachedDateTimeFormatter(this, cacheSize = cacheSize)
}

