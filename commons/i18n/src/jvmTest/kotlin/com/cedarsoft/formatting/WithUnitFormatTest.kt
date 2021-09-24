package com.cedarsoft.formatting

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class WithUnitFormatTest {
  @Test
  fun testIt() {
    WithUnitFormat(decimalFormat, "daUnit").let {
      assertThat(it.format(17.01234)).isEqualTo("17.01 daUnit")
    }
  }
}
