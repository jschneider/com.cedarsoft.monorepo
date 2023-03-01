package it.neckar.open.concurrent

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
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
