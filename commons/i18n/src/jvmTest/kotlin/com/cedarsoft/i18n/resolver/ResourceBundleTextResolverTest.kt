package com.cedarsoft.i18n.resolver

import assertk.*
import assertk.assertions.*
import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.TextKey
import com.cedarsoft.test.utils.WithLocale
import com.cedarsoft.time.TimeZone
import org.junit.jupiter.api.Test


@WithLocale("fr_FR")
class ResourceBundleTextResolverTest {
  private val bundle: String = "com.cedarsoft.i18n.test.testmessages"

  @Test
  fun testBundles() {
    val resolver = ResourceBundleTextResolver(bundle)
    assertThat(resolver).isNotNull()
    assertThat(resolver.bundleName).isEqualTo(bundle)
    assertThat(resolver.resolve(TextKey("ERROR_1"), I18nConfiguration.Germany)).isEqualTo("The Value 1 de: <{0}>")
    assertThat(resolver.resolve(TextKey("ERROR_1"), I18nConfiguration.US)).isEqualTo("The Value 1 en: <{0}>")
    assertThat(resolver.resolve(TextKey("ERROR_1"), I18nConfiguration.French)).isEqualTo("The Value 1 default: <{0}>")
  }
}

private val I18nConfiguration.Companion.French: I18nConfiguration
  get() {
    return I18nConfiguration(TimeZone.Berlin, com.cedarsoft.i18n.Locale.France)
  }
