package it.neckar.open.i18n.resolver

import assertk.*
import assertk.assertions.*
import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.TextKey
import it.neckar.open.test.utils.WithLocale
import it.neckar.open.time.TimeZone
import org.junit.jupiter.api.Test


@WithLocale("fr_FR")
class ResourceBundleTextResolverTest {
  private val bundle: String = "it.neckar.open.i18n.test.testmessages"

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
    return I18nConfiguration(TimeZone.Berlin, it.neckar.open.i18n.Locale.France)
  }
