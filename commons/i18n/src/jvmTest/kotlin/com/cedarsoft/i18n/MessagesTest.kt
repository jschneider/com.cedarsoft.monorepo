package com.cedarsoft.i18n

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.WithLocale
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Locale


@WithLocale("fr_FR")
class MessagesTest {

  private val bundle: String = "com.cedarsoft.i18n.test.testmessages"

  @Test
  fun testBundles() {
    val messages = Messages(bundle)
    assertThat(messages).isNotNull()
    assertThat(messages.bundleName).isEqualTo(bundle)
    assertThat(messages["ERROR_1", Locale.GERMAN]).isEqualTo("The Value 1 de: <{0}>")
    assertThat(messages["ERROR_1", Locale.ENGLISH]).isEqualTo("The Value 1 en: <{0}>")
    assertThat(messages["ERROR_1", Locale.FRENCH]).isEqualTo("The Value 1 default: <{0}>")
  }
}

