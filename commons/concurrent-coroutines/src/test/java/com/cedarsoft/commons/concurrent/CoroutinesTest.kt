package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.*
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


    println("done")
  }
}
