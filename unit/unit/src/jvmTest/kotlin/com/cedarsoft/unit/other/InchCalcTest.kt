package com.cedarsoft.unit.other

import assertk.*
import assertk.assertions.*
import com.cedarsoft.unit.si.mm
import org.junit.jupiter.api.Test

/**
 *
 */
class InchCalcTest {
  @Test
  @Throws(Exception::class)
  fun testIt() {
    @mm val mm = 100
    @`in` val inInch = mm.toDouble() / `in`.MM_RATIO
    assertThat(inInch).isEqualTo(3.937007874015748)
  }

  @Test
  @Throws(java.lang.Exception::class)
  fun testReturn() {
    @`in` val inches = 1
    @mm val mm = inches * `in`.MM_RATIO
    assertThat(mm).isEqualTo(25.4)
  }
}
