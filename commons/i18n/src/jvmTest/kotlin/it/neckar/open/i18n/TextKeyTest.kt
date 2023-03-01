package it.neckar.open.i18n

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class TextKeyTest {
  @Test
  fun testEqualsHashCode() {
    assertThat(it.neckar.open.i18n.TextKey("asdf", "daText")).isEqualTo(it.neckar.open.i18n.TextKey("asdf", "other text"))
    assertThat(it.neckar.open.i18n.TextKey("asdf", "daText").hashCode()).isEqualTo(it.neckar.open.i18n.TextKey("asdf", "other text").hashCode())

    assertThat(it.neckar.open.i18n.TextKey("asdf", "daText")).isNotEqualTo(it.neckar.open.i18n.TextKey("otherKey", "other text"))
  }
}
