package it.neckar.open.i18n

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.WithLocale
import org.junit.jupiter.api.Test
import java.util.Locale

class DefaultLocaleProviderKtTest {
  @WithLocale("de_DE")
  @Test
  fun testLocaleStuff() {
    assertThat(Locale.getDefault().language).isEqualTo("de")
    assertThat(Locale.getDefault().country).isEqualTo("DE")
    assertThat(Locale.getDefault().displayName).isEqualTo("Deutsch (Deutschland)")
    assertThat(Locale.getDefault().displayScript).isEqualTo("")
    assertThat(Locale.getDefault().displayVariant).isEqualTo("")
    assertThat(Locale.getDefault().toLanguageTag()).isEqualTo("de-DE")
    assertThat(Locale.getDefault().country).isEqualTo("DE")
    assertThat(Locale.getDefault().language).isEqualTo("de")
    assertThat(Locale.getDefault().toString()).isEqualTo("de_DE")
  }

  @Test
  internal fun testOnlyLanguage() {
    assertThat(it.neckar.open.i18n.Locale("en").convert().language).isEqualTo("en")
    assertThat(it.neckar.open.i18n.Locale("en").convert().country).isEqualTo("")
  }

  @WithLocale("de_DE")
  @Test
  fun testGetDefaultLocaleDeDE() {
    val locale = DefaultLocaleProvider().defaultLocale
    assertThat(locale.locale).isEqualTo("de-DE")
  }

  @WithLocale("en_US")
  @Test
  fun testGetDefaultLocaleEnUs() {
    val locale = DefaultLocaleProvider().defaultLocale
    assertThat(locale.locale).isEqualTo("en-US")
  }

  @WithLocale("en_US")
  @Test
  fun testConvertBack() {
    assertThat(it.neckar.open.i18n.Locale("de-DE").convert()).isEqualTo(Locale.GERMANY)
    assertThat(it.neckar.open.i18n.Locale("en-US").convert()).isEqualTo(Locale.US)
  }

  @Test
  fun testConvertRound() {
    testRoundTrip(Locale.GERMANY)
    testRoundTrip(Locale.US)
    testRoundTrip(Locale.GERMAN)
    testRoundTrip(Locale.ENGLISH)
  }

  private fun testRoundTrip(locale: Locale) {
    val converted = locale.convert()
    assertThat(converted.convert(), "Locale: <$locale>, Converted <$converted>").isEqualTo(locale)
  }
}
