package com.cedarsoft.formatting

import com.cedarsoft.common.collections.cache
import com.cedarsoft.i18n.convert
import java.text.NumberFormat

/**
 * A cache for number formats for a locale
 */
class NumberFormatCache(
  val description: String,
  /**
   * Configures the number format
   */
  val configure: NumberFormat.() -> Unit
) {
  private val cache = cache<com.cedarsoft.i18n.Locale, NumberFormat>("NumberFormatCache <$description>", 10)

  /**
   * Returns the cached number format for the given locale
   */
  operator fun get(locale: com.cedarsoft.i18n.Locale): NumberFormat {
    return cache.getOrStore(locale) {
      NumberFormat.getInstance(locale.convert()).also {
        it.configure()
      }
    }
  }
}
