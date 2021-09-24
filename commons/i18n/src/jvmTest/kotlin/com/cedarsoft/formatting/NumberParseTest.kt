package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class NumberParseTest {
  @Test
  fun testIt() {
    assertThat("12000.0".toDoubleOrNull()).isEqualTo(12_000.0)
    assertThat("12000".toDoubleOrNull()).isEqualTo(12_000.0)
    //assertThat("12,000".toDoubleOrNull()).isEqualTo(12_000.0) //TODO does *NOT* work
  }
}
