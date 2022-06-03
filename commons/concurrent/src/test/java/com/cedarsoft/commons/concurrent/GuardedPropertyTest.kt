package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 */
internal class GuardedPropertyTest {
  @Test
  internal fun testIt() {
    val guardedByLock = GuardedProperty(7)

    assertThat(guardedByLock.get()).isEqualTo(7)
    guardedByLock.set(17)
    assertThat(guardedByLock.get()).isEqualTo(17)

    guardedByLock.read {
      val currentValue = get()
      assertThat(currentValue).isEqualTo(17)
    }

    guardedByLock.write {
      val oldValue = get()
      assertThat(oldValue).isEqualTo(17)

      set(oldValue + 1)
    }

    assertThat(guardedByLock.get()).isEqualTo(18)
  }
}
