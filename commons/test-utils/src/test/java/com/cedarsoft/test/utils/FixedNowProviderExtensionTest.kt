package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.time.nowMillis
import org.junit.jupiter.api.Test

/**
 *
 */
class FixedNowProviderExtensionTest {
  @FixedTime(5000.0)
  @Test
  internal fun testIt() {
    assertThat(nowMillis()).isEqualTo(5000.0)
  }
}
