package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

/**
 *
 */
class FlowDemo {
  @Test
  fun testFlowStuff() = runTest {
    val flow = flow {
      repeat(10) {
        println("Emitting $it @ $currentTime")
        emit("$it")
        delay(100)
      }

      delay(500)
    }

    flow
      //.debounce(150)
      .sample(150)
      .collectLatest {
        println("$it @ $currentTime")
      }
  }
}
