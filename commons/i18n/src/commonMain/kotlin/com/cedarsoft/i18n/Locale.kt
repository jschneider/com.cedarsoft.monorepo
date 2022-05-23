package com.cedarsoft.i18n

/**
 * Represents a locale.
 *
 * Uses the language tag format (e.g. "en-US")
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
data class Locale(val locale: String) {
  override fun toString(): String {
    return locale
  }

  companion object {
    val US: Locale = Locale("en-US")
    val Germany: Locale = Locale("de-DE")
    val France: Locale = Locale("fr-FR")
  }
}
