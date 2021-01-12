package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@ExperimentalCoroutinesApi
class CoAsyncTest {

  @Disabled //long running test
  @Test
  fun testArrayBroadcastChannel() {
    runBlocking {
      val channel = BroadcastChannel<Int>(17)

      launch {
        for (i in 1..100) {
          channel.send(i)
          delay(40)
        }

        channel.close()
      }


      channel.openSubscription()
        .consumeEach {
          println("Consumed $it")
        }

    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun testIt() {
    runBlocking {
      val async = CoAsync().start(CoroutineScope(Dispatchers.Default))

      val calledFor = AtomicInteger()
      val runCount = AtomicInteger()

      val finished = AtomicBoolean()

      //create threads that schedule updates
      val job = async(Dispatchers.Default) {
        for (i in 1..70_000) {
          async.last {
            delay(5)
            calledFor.set(i)
            runCount.incrementAndGet()
          }
        }

        finished.set(true)
      }

      job.join()
      Awaitility.await().untilAtomic(calledFor, CoreMatchers.`is`(70_000))
      assertThat(finished.get()).isTrue()
      assertThat(calledFor.get()).isEqualTo(70000)
      assertThat(runCount.get()).isLessThan(70000 - 1)
    }
  }

  @Test
  fun testDelay() {
    runBlocking {
      val async = CoAsync().start(CoroutineScope(Dispatchers.Default))

      val calledFor = AtomicInteger()
      val runCount = AtomicInteger()

      val finishedAdding = AtomicBoolean()
      val finishedRun = AtomicBoolean()

      //create threads that schedule updates
      val job = async(Dispatchers.Default) {
        for (i in 1..30) {
          async.last {
            calledFor.set(i)
            runCount.incrementAndGet()
            delay(100)

            if (finishedAdding.get()) {
              finishedRun.set(true)
            }
          }
        }

        finishedAdding.set(true)
      }

      job.join()
      Awaitility.await().untilAtomicIsTrue(finishedAdding)
      Awaitility.await().untilAtomicIsTrue(finishedRun)

      assertThat(calledFor.get()).isEqualTo(30)
      assertThat(runCount.get()).isLessThan(3)
    }
  }

  private fun println(message: String) {
    logger.info(message)
  }

  companion object {
    val logger: org.slf4j.Logger = LoggerFactory.getLogger(CoAsyncTest::class.java)
  }
}
