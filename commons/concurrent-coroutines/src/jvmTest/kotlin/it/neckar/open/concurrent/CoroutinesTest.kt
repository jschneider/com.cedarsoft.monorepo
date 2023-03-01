package it.neckar.open.concurrent

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 *
 */
class CoroutinesTest {
  @Test
  fun testIt() = runBlocking {
    val context = newSingleThreadContext("asdf")

    withContext(context) {
      assertThat(Thread.currentThread().name).startsWith("asdf")

      var runInner = false

      withContext(context) {
        assertThat(Thread.currentThread().name).startsWith("asdf")
        assertThat(runInner).isFalse()
        runInner = true
      }

      assertThat(Thread.currentThread().name).startsWith("asdf")
      assertThat(runInner).isTrue()
    }
  }

  @Disabled("Does not run, only as compile time check")
  @Test
  fun test() = runBlocking {
    withTimeout(1300L) {
      repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
      }
    }
  }
}
