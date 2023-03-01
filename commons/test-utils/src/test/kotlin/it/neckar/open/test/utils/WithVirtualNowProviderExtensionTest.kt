package it.neckar.open.test.utils

import assertk.*
import assertk.assertions.*
import it.neckar.open.time.VirtualNowProvider
import it.neckar.open.time.nowMillis
import it.neckar.open.time.nowProvider
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
