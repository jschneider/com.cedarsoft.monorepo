package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class StringExtKtTest {
  @Test
  fun testCssIdentifier() {
    assertThat("asdf".encodeForCssIdentifier()).isEqualTo("asdf")
    assertThat("as df".encodeForCssIdentifier()).isEqualTo("as_df")
    assertThat("""as :\/df""".encodeForCssIdentifier()).isEqualTo("as____df")
  }

  @Test
  fun testDeleteTrailing() {
    assertThat(buildString {
      deleteSuffix("x")
    }).isEqualTo("")

    assertThat(buildString {
      append("Hello World!x")
      deleteSuffix("x")
    }).isEqualTo("Hello World!")

    assertThat(buildString {
      append("Hello World!x!")
      deleteSuffix("x")
    }).isEqualTo("Hello World!x!")

    assertThat(buildString {
      append("Hello World!x!")
      deleteSuffix("!x!")
    }).isEqualTo("Hello World")
  }

  @Test
  fun testSplit() {
    assertThat("asdf".centerIndexOf(' ')).isEqualTo(-1)
    assertThat("as df".centerIndexOf(' ')).isEqualTo(2)
    assertThat("as dffsdfsdfsdf".centerIndexOf(' ')).isEqualTo(2)
    assertThat("0123456789 dddddd".centerIndexOf(' ')).isEqualTo(10)
    assertThat("0 23456789 dddddd".centerIndexOf(' ')).isEqualTo(10)
    assertThat("0 23456789 dddd d".centerIndexOf(' ')).isEqualTo(10)
  }

  @Test
  fun testTakeIfNotBlank() {
    assertThat("asdf".nullIfBlank()).isEqualTo("asdf")
    assertThat((null as String?).nullIfBlank()).isNull()
    assertThat(("  ").nullIfBlank()).isNull()
  }

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
