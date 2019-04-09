package com.cedarsoft.test.utils

import assertk.assert
import assertk.assertAll
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.length
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assert
import kotlin.test.assertTrue

/**
 * Shows basic usages for assertk
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class AssertKSampleTest {
  @Test
  internal fun testFunction1() {
    assertThat(true).isTrue()
  }

  @Test
  internal fun testException() {
    assertThrows<ArithmeticException> {
      @Suppress("DIVISION_BY_ZERO")
      5 / 0
    }

    assertTrue {
      val a = 1 + 2
      a > 2
    }
  }

  @Test
  internal fun testAssertk() {
    assert("asdf").isNotEmpty()

    val elements = listOf("a", "b", "c")
    assert(elements).hasSize(3)

    val myString: String? = "asdf"
    assert(myString).isNotNull {
      it.length().isEqualTo(4)
    }

    assertAll {
//      assert("asdf").isEmpty()
      assert("asdf").isNotEmpty()
      assert("asdf").length().isEqualTo(4)
    }
  }
}
