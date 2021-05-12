package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class StringExtKtTest {
  @Test
  fun testFileEncode() {
    assertThat("asdf".encodeForFileName()).isEqualTo("asdf")
    assertThat("äöüß?#+*".encodeForFileName()).isEqualTo("äöüß_#+_")
    assertThat("/".encodeForFileName()).isEqualTo("_")
    assertThat("\\".encodeForFileName()).isEqualTo("_")
  }
}
