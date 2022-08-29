package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.time.VirtualNowProvider
import com.cedarsoft.common.time.nowMillis
import com.cedarsoft.common.time.nowProvider
import org.junit.jupiter.api.Test

/**
 *
 */
@VirtualTime(5000.0)
class WithVirtualNowProviderExtensionTest {
  @Test
  fun testNotParam() {
    assertThat(nowProvider)
      .isNotNull()
      .isInstanceOf(VirtualNowProvider::class.java)

    assertThat(nowMillis()).isEqualTo(5000.0)
  }

  @Test
  fun testIt(nowProvider: VirtualNowProvider) {
    assertThat(nowProvider)
      .isNotNull()
      .isSameAs(nowProvider)

    assertThat(nowMillis()).isEqualTo(5000.0)
  }

  @Test
  fun testTimeNotUpdatingDelayed() {
    assertThat(nowMillis()).isEqualTo(5000.0)
    Thread.sleep(10)
    assertThat(nowMillis()).isEqualTo(5000.0)
  }
}
