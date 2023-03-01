package it.neckar.open.test.utils

import assertk.*
import assertk.assertions.*
import it.neckar.open.time.nowMillis
import org.junit.jupiter.api.Test

/**
 *
 */
class VirtualNowProviderExtensionTest {
  @VirtualTime(5000.0)
  @Test
  fun testIt() {
    assertThat(nowMillis()).isEqualTo(5000.0)
  }
}
