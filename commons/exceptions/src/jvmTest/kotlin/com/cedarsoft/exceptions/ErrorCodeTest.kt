package com.cedarsoft.exceptions

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class ErrorCodeTest {
  @Test
  fun testPrefixValid() {
    assertThat(ErrorCode.create("ThePrefix", 11).format()).isEqualTo("ThePrefix-11")

    try {
      ErrorCode.create("The-Prefix", 11)
      fail("Where is the exception?")
    } catch (_: Exception) {
    }
  }

  @Test
  fun testParse() {
    verifyParseRoundtrip(ErrorCode.create("PRE", 12))
    verifyParseRoundtrip(ErrorCode.create("PRE1", 17))
  }

  private fun verifyParseRoundtrip(errorCode: ErrorCode) {
    val parsed = ErrorCode.parse(errorCode.format())
    assertThat(parsed).isEqualTo(errorCode)
  }
}
