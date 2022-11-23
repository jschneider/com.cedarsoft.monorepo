package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

fun main() {
  //FlowDemo().testFlowStuff()
  FlowDemo().testFlowOn()
}

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

  @Test
  fun testFlowOn() = runTest {
    val myFlow = flow {
      emit(1)
      coroutineScope {
        emit(2)
      }
    }

    myFlow.collect {
      println("collected: $it")
    }
  }
}
