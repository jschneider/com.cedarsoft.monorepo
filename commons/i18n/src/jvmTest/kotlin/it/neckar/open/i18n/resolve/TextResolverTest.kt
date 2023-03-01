package it.neckar.open.i18n.resolve

import assertk.*
import assertk.assertions.*
import it.neckar.open.i18n.I18nConfiguration
import it.neckar.open.i18n.Locale
import it.neckar.open.i18n.TextKey
import it.neckar.open.time.TimeZone
import org.junit.jupiter.api.Test

class TextResolverTest {
  val usConfiguration: I18nConfiguration = I18nConfiguration(
    textLocale = Locale.US,
    formatLocale = Locale.US,
    timeZone = TimeZone.UTC
  )

  val deConfiguration: I18nConfiguration = I18nConfiguration(
    textLocale = Locale.Germany,
    formatLocale = Locale.Germany,
    timeZone = TimeZone.Berlin
  )

  val blConfiguration: I18nConfiguration = I18nConfiguration(
    textLocale = Locale("bl_BL"),
    formatLocale = Locale("bl_BL"),
    timeZone = TimeZone.Berlin
  )

  @Test
  fun testFallbackLocaleTextProvider() {
    val textResolver = MapBasedTextResolver()
    val key = TextKey("asdf", "ddd")
    textResolver.setText(Locale.US, key, "translationUs")

    val fallbackProvider = ForceLocaleTextResolver(Locale.US, textResolver)

    assertThat(textResolver.resolve(key, usConfiguration)).isEqualTo("translationUs")
    assertThat(textResolver.resolve(key, deConfiguration)).isNull()

    assertThat(fallbackProvider.resolve(key, usConfiguration)).isEqualTo("translationUs")
    assertThat(fallbackProvider.resolve(key, deConfiguration)).isEqualTo("translationUs")
    assertThat(fallbackProvider.resolve(key, blConfiguration)).isEqualTo("translationUs")
  }

  @Test
  fun testSetTextSimple() {
    val textProvider = SimpleMapBasedTextResolver()
    val textKey = TextKey("abc", "def text abc")

    assertThat(textProvider.resolve(textKey, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey, deConfiguration)).isNull()

    textProvider.setText(textKey, "foobar")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("foobar")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("foobar")

    textProvider.setText(textKey, "Hello World")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("Hello World")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("Hello World")

    textProvider.setText(textKey, "")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("")

    textProvider.setText(textKey, "Hello World")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("Hello World")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("Hello World")

    textProvider.setText(textKey, null)
    assertThat(textProvider.resolve(textKey, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey, deConfiguration)).isNull()
  }

  @Test
  fun testSetTextsSimple() {
    val textProvider = SimpleMapBasedTextResolver()
    val textKey1 = TextKey("abc", "def text abc")
    val textKey2 = TextKey("def", "def text def")

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isNull()

    textProvider.setTexts(mapOf(Pair(textKey1, "1"), Pair(textKey2, "2")))

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("2")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("2")

    textProvider.setTexts(mapOf(Pair(textKey1, "a"), Pair(textKey2, "b")))

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("a")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("a")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("b")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("b")

    textProvider.setTexts(mapOf(Pair(textKey1, ""), Pair(textKey2, "")))

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("")

    textProvider.setTexts(mapOf(Pair(textKey1, "1"), Pair(textKey2, "2")))

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("2")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("2")

    textProvider.setTexts(mapOf<TextKey, String?>(Pair(textKey1, null), Pair(textKey2, null)))

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isNull()
  }

  @Test
  fun testSetTextLocaleAware() {
    val textProvider = MapBasedTextResolver()
    val textKey = TextKey("abc", "def text")

    assertThat(textProvider.resolve(textKey, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey, deConfiguration)).isNull()

    textProvider.setText(Locale.US, textKey, "a")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("a")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isNull()

    textProvider.setText(Locale.Germany, textKey, "b")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("a")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("b")

    textProvider.setText(Locale.US, textKey, "")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("b")

    textProvider.setText(Locale.Germany, textKey, "")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("")

    textProvider.setText(Locale.US, textKey, "1")
    textProvider.setText(Locale.Germany, textKey, "2")
    assertThat(textProvider.resolve(textKey, usConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("2")

    textProvider.setText(Locale.US, textKey, null)
    assertThat(textProvider.resolve(textKey, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey, deConfiguration)).isEqualTo("2")

    textProvider.setText(Locale.Germany, textKey, null)
    assertThat(textProvider.resolve(textKey, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey, deConfiguration)).isNull()
  }

  @Test
  fun testSetTextsLocaleAware() {
    val textProvider = MapBasedTextResolver()
    val textKey1 = TextKey("abc", "Def abc")
    val textKey2 = TextKey("def", "Def Text def")

    assertThat(textProvider.resolve(textKey1, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isNull()

    textProvider.setTexts(mapOf(Pair(Locale.US, mapOf(Pair(textKey1, "a"), Pair(textKey2, "b")))))
    textProvider.setTexts(mapOf(Pair(Locale.Germany, mapOf(Pair(textKey1, "1"), Pair(textKey2, "2")))))
    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("a")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("b")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("2")

    textProvider.setTexts(mapOf(Pair(Locale.US, mapOf(Pair(textKey1, ""), Pair(textKey2, "")))))
    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isEqualTo("1")
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isEqualTo("2")

    textProvider.setTexts(mapOf(Pair(Locale.Germany, mapOf(Pair(textKey1, null), Pair(textKey2, null)))))
    assertThat(textProvider.resolve(textKey1, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey2, usConfiguration)).isEqualTo("")
    assertThat(textProvider.resolve(textKey1, deConfiguration)).isNull()
    assertThat(textProvider.resolve(textKey2, deConfiguration)).isNull()
  }
}
