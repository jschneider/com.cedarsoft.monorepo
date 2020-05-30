package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class GuardedByLockTest {
  @Test
  internal fun testSimple() {
    val s = GuardedByLock(ReentrantReadWriteLock(), "asdf")

    s.read {
      assertThat(this).isEqualTo("asdf")
    }

    s.write {
      assertThat(this).isEqualTo("asdf")
    }
  }
}
