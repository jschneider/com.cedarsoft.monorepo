package com.cedarsoft.i18n.resolve

import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.Locale
import com.cedarsoft.i18n.TextKey
import com.cedarsoft.i18n.TextResolver

/**
 * Returns the translation for a defined locale
 */
class ForceLocaleTextResolver(
  var locale: Locale,
  val delegate: TextResolver
) : TextResolver {
  override fun resolve(key: TextKey, i18nConfiguration: I18nConfiguration): String? {
    return delegate.resolve(key, i18nConfiguration.copy(textLocale = locale)) //Do *not* use the requested locale but the fallback locale instead
  }
}
