package it.neckar.open.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class AssertionsKtTest {
  @Test
  fun testRequiresEquals() {
    requireEquals(1, 1) { "asdf" }
    requireEquals(2, 2) { "asdf" }

    try {
      requireEquals(1, 2) { "asdf" }
      fail("Where is the exception?")
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("asdf: <1> != <2>")
    }
  }

  @Test
  fun testCheckEquals() {
    checkEquals(1, 1) { "asdf" }
    checkEquals(2, 2) { "asdf" }

    try {
      checkEquals(1, 2) { "asdf" }
      fail("Where is the exception?")
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessage("asdf: <1> != <2>")
    }
  }
}
