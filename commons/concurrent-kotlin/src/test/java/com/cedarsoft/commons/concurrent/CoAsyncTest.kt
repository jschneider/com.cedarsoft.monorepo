package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class CoAsyncTest {
  @ExperimentalCoroutinesApi
  @Test
  internal fun testIt() {
    runBlocking {
      val async = CoAsync().start(CoroutineScope(Dispatchers.Default))

      val calledFor = AtomicInteger()
      val runCount = AtomicInteger()

      //create threads that schedule updates
      val job = async(Dispatchers.Default) {
        for (i in 1..70000) {
          async.last {
            calledFor.set(i)
            runCount.incrementAndGet()
          }
        }
      }

      job.join()
      delay(100)

      assertThat(calledFor.get()).isEqualTo(70000)
      assertThat(runCount.get()).isLessThan(70000 - 1)

      println("done")
    }
  }


  private fun println(message: String) {
    logger.info(message)
  }

  companion object {
    val logger: Logger = Logger.getLogger(CoAsyncTest::class.qualifiedName)
  }
}