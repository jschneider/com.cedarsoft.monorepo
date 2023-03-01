package it.neckar.open.test.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

/**
 */
@WithLocale("ja_JP")
class LocaleExtensionTest {
  @Test
  fun testWithout() {
    //Value from "global" config for complete test
    assertThat(Locale.getDefault()).isEqualTo(Locale.JAPAN)
  }

  @WithLocale("de_DE")
  @Test
  fun testDe() {
    assertThat(Locale.getDefault()).isEqualTo(Locale.GERMANY)
  }

  @WithLocale("en_US")
  @Test
  fun testUs() {
    assertThat(Locale.getDefault()).isEqualTo(Locale.US)
  }
}
