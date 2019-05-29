package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.*
import org.awaitility.Awaitility
import org.awaitility.core.ConditionFactory
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@ExperimentalCoroutinesApi
internal class CoAsyncTest {
  @ExperimentalCoroutinesApi
  @Test
  internal fun testIt() {
    runBlocking {
      val async = CoAsync().start(CoroutineScope(Dispatchers.Default))

      val calledFor = AtomicInteger()
      val runCount = AtomicInteger()

      val finished = AtomicBoolean()

      //create threads that schedule updates
      val job = async(Dispatchers.Default) {
        for (i in 1..70000) {
          async.last {
            calledFor.set(i)
            runCount.incrementAndGet()
          }
        }

        finished.set(true)
      }

      job.join()
      Awaitility.await().untilAtomic(finished, CoreMatchers.`is`(true))

      assertThat(calledFor.get()).isEqualTo(70000)
      assertThat(runCount.get()).isLessThan(70000 - 1)
    }
  }

  @Test
  internal fun testDelay() {
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
      Awaitility.await().untilAtomic(finishedAdding)
      Awaitility.await().untilAtomic(finishedRun)

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

private fun ConditionFactory.untilAtomic(atomicBoolean: AtomicBoolean) {
  return untilAtomic(atomicBoolean, CoreMatchers.`is`(true))
}
