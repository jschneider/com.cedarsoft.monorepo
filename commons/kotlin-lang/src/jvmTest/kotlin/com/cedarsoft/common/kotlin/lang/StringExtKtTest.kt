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

  @Test
  fun testWrap() {
    val text = "This is a text that is wrapped"
    assertThat(text.wrap(10)).containsExactly("This is a", "text that", "is wrapped")
    assertThat(text.wrap(5)).containsExactly("This", "is a", "text", "that", "is", "wrapp", "ed")
    assertThat(text.wrap(2)).containsExactly("Th", "is", "is", "a", "te", "xt", "th", "at", "is", "wr", "ap", "pe", "d")
    assertThat(text.wrap(9)).containsExactly("This is", "a text", "that is", "wrapped")

    for (i in 2..20) {
      val wrapped = text.wrap(i)
      wrapped.forEach {
        assertThat(it.length, "length: $i, wrapped: $wrapped ").isLessThanOrEqualTo(i)
      }
    }
  }

  @Test
  fun testWrapEdgeCases() {
    assertThat("".wrap(10)).isEmpty()
    assertThat("a".wrap(10)).containsExactly("a")
    assertThat(" ".wrap(10)).containsExactly(" ")
    assertThat("abc".wrap(10)).containsExactly("abc")
    assertThat("a bc".wrap(10)).containsExactly("a bc")

    assertThat(" a bc".wrap(2)).containsExactly(" a", "bc")
    assertThat(" a bc ".wrap(2)).containsExactly(" a", "bc")
    assertThat(" a bc".wrap(2)).containsExactly(" a", "bc")
    assertThat(" a  bc".wrap(2)).containsExactly(" a", " b", "c")
    assertThat(" a  bc ".wrap(2)).containsExactly(" a", " b", "c ")
  }
}
