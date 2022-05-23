package com.cedarsoft.i18n.resolve

import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.TextKey
import com.cedarsoft.i18n.TextResolver

/**
 * Returns the text key as text.
 * Used as fallback
 */
object TextKeyTextResolver : TextResolver {
  override fun resolve(key: TextKey, i18nConfiguration: I18nConfiguration): String? {
    return key.key
  }
}
