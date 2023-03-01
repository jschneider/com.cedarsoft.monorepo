package it.neckar.open.concurrent

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.untilAtomicIsTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 */
@ExperimentalCoroutinesApi
class CoAsyncTest {

  @Test()
  fun testExceptionInJob() {
    runBlocking {
      val async = CoAsync()
      async.start(CoroutineScope(Dispatchers.Default))

      try {
        var counter = 0

        async.last {
          assertThat(counter).isEqualTo(0)
          counter++
          throw UnsupportedOperationException()
        }

        withTimeout(10_000) {
          while (counter != 1) {
            delay(10)
          }
        }

        async.last {
          assertThat(counter).isEqualTo(1)
          counter++
          throw UnsupportedOperationException()
        }

        withTimeout(10_000) {
          while (counter != 2) {
            delay(10)
          }
        }

        assertThat(counter).isEqualTo(2)
      } finally {
        async.dispose()
      }
    }
  }

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
      val async = CoAsync()
      async.start(CoroutineScope(Dispatchers.Default))

      try {
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
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAtomic(calledFor, CoreMatchers.`is`(70_000))
        assertThat(finished.get()).isTrue()
        assertThat(calledFor.get()).isEqualTo(70000)
        assertThat(runCount.get()).isLessThan(70000 - 1)
      } finally {
        async.dispose()
      }
    }
  }

  @Test
  fun testDelay() {
    runBlocking {
      val async = CoAsync()
      async.start(CoroutineScope(Dispatchers.Default))

      try {
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
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAtomicIsTrue(finishedAdding)
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAtomicIsTrue(finishedRun)

        assertThat(calledFor.get()).isEqualTo(30)
        assertThat(runCount.get()).isLessThan(3)
      } finally {
        async.dispose()
      }
    }
  }

  private fun println(message: String) {
    logger.info(message)
  }

  companion object {
    val logger: org.slf4j.Logger = LoggerFactory.getLogger(CoAsyncTest::class.java)
  }
}
