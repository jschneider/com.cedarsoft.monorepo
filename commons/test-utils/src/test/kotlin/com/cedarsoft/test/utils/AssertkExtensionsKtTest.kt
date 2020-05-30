package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class AssertkExtensionsKtTest {
  @Test
  fun testNaN() {
    assertThat {
      assertThat(70.0).isNaN()
    }.isFailure().hasMessage("expected to be NaN but was <70.0>")
  }
}
