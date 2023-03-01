package it.neckar.open.concurrent

import it.neckar.open.kotlin.lang.random
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 *
 */
class HeartbeatTest {
  @Disabled("Does not run, only as compile time check")
  @Test
  fun testIt() = runBlocking {

    val heartbeat = Heartbeat {
      val latency = random.nextLong(150)
      delay(latency)
      if (latency > 100.0) {
        ConnectionFailure
      } else {
        Alive(latency.toDouble())
      }
    }

    val heartBeat = heartbeat.runHeartBeat()

    heartBeat.collect {
      println("--> $it")
    }
  }

  @Test
  fun testMutableFlow() = runBlocking {
    val responseTime = flow {
      val result = withTimeoutOrNull(300L) {
        repeat(50) {
          val value = random.nextLong(100)

          delay(value)
          println("emitting $it on ${Thread.currentThread()}")
          emit(it)
        }
      }

      println("Result: $result")
    }

    println("flow has been created")

    responseTime
      .conflate()
      .filter {
        it % 5 == 0
      }.flowOn(Dispatchers.IO)
      .collect {
        delay(50)
        println("Collecting $it")
      }

    println("Done")
  }
}
