package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.time.FixedNowProvider
import com.cedarsoft.common.time.nowMillis
import com.cedarsoft.common.time.nowProvider
import org.junit.jupiter.api.Test

/**
 *
 */
@FixedTime(5000.0)
class WithFixedNowProviderExtensionTest {
  @Test
  fun testNotParam() {
    assertThat(nowProvider)
      .isNotNull()
      .isInstanceOf(FixedNowProvider::class.java)

    assertThat(nowMillis()).isEqualTo(5000.0)
  }

  @Test
  fun testIt(nowProvider: FixedNowProvider) {
    assertThat(nowProvider)
      .isNotNull()
      .isSameAs(nowProvider)

    assertThat(nowMillis()).isEqualTo(5000.0)
  }
}
