package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 */
class ObservableStringTest {
  @Test
  internal fun testIt() {
    val observableString = ObservableString("asdf")
    assertThat(observableString.value).isEqualTo("asdf")
  }
}
