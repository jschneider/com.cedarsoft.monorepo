package com.cedarsoft.i18n

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class TextKeyTest {
  @Test
  fun testEqualsHashCode() {
    assertThat(com.cedarsoft.i18n.TextKey("asdf", "daText")).isEqualTo(com.cedarsoft.i18n.TextKey("asdf", "other text"))
    assertThat(com.cedarsoft.i18n.TextKey("asdf", "daText").hashCode()).isEqualTo(com.cedarsoft.i18n.TextKey("asdf", "other text").hashCode())

    assertThat(com.cedarsoft.i18n.TextKey("asdf", "daText")).isNotEqualTo(com.cedarsoft.i18n.TextKey("otherKey", "other text"))
  }
}
