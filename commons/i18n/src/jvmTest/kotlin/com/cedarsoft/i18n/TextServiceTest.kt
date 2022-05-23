package com.cedarsoft.i18n

import assertk.*
import assertk.assertions.*
import com.cedarsoft.i18n.resolve.MapBasedTextResolver
import com.cedarsoft.i18n.resolve.SimpleMapBasedTextResolver
import org.junit.jupiter.api.Test

/**
 *
 */
class TextServiceTest {
  @Test
  internal fun testCreate() {
    val service = SimpleMapBasedTextResolver().orFallbackText()
    assertThat(service[TextKey("asdf"), I18nConfiguration.Germany]).isEqualTo("asdf")
  }

  @Test
  fun testFallback() {
    val textService = TextService()
    textService.addTextKeyTextResolver()
    assertThat(textService[TextKey("asdf", "defa"), I18nConfiguration.US]).isEqualTo("asdf")
  }

  @Test
  fun testDef() {
    val textService = TextService()
    textService.addFallbackTextResolver()
    assertThat(textService[TextKey("asdf", "defa"), I18nConfiguration.US]).isEqualTo("defa")
  }

  @Test
  fun testTextService() {
    val textService = TextService()
    val textKey = TextKey("abc", "defa")

    //Use the text key as text
    assertThat(textService[textKey, I18nConfiguration.US]).isEqualTo(textKey.key)

    val provider1 = SimpleMapBasedTextResolver()
    val provider2 = MapBasedTextResolver()

    textService.addTextResolverAtFirst(provider2)
    textService.addTextResolverAtFirst(provider1)

    provider2.setText(Locale.US, textKey, "a")
    assertThat(textService[textKey, I18nConfiguration.US]).isEqualTo("a")
    assertThat(textService[textKey, I18nConfiguration.Germany]).isEqualTo(textKey.key)

    provider1.setText(textKey, "b")
    assertThat(textService[textKey, I18nConfiguration.US]).isEqualTo("b")
    assertThat(textService[textKey, I18nConfiguration.Germany]).isEqualTo("b")

    textService.removeTextResolver(provider1)
    assertThat(textService[textKey, I18nConfiguration.US]).isEqualTo("a")
    assertThat(textService[textKey, I18nConfiguration.Germany]).isEqualTo(textKey.key)
  }

  @Test
  fun testEnum() {
    val textService = TextService()

    assertThat(textService[MyEnum.Option1, I18nConfiguration.Germany]).isEqualTo("Option1")
    assertThat(textService[MyEnum.Option2, I18nConfiguration.Germany]).isEqualTo("Option2")

    assertThat(textService[MyEnum.Option1, I18nConfiguration.Germany, "cat1"]).isEqualTo("Option1.cat1")
    assertThat(textService[MyEnum.Option2, I18nConfiguration.Germany, "cat2"]).isEqualTo("Option2.cat2")
  }
}

enum class MyEnum {
  Option1, Option2
}
