package com.cedarsoft.i18n.resolver

import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.Locale
import com.cedarsoft.i18n.TextKey
import com.cedarsoft.i18n.TextResolver
import java.util.ResourceBundle

/**
 * Type alias to simplify conversion from deprecated messages class
 */
typealias Messages = ResourceBundleTextResolver

/**
 * Uses [java.util.ResourceBundle] to resolve a text
 */
class ResourceBundleTextResolver(
  val bundleName: String
) : TextResolver {

  override fun resolve(key: TextKey, i18nConfiguration: I18nConfiguration): String? {
    val bundle = ResourceBundle.getBundle(bundleName, i18nConfiguration.textLocale.toJvmLocale())
    return bundle.getString(key.key)
  }
}

private fun Locale.toJvmLocale(): java.util.Locale {
  return java.util.Locale.forLanguageTag(this.locale)
}
