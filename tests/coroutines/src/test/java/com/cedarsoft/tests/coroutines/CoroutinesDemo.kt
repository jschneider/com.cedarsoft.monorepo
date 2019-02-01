package com.cedarsoft.tests.coroutines


import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
  CoroutinesDemo().run()
}

class CoroutinesDemo {
  fun run() = runBlocking {
    val job = launch {
      try {
        repeat(1000) { i ->
          logger.info("I'm sleeping $i ...")
          delay(500L)
        }
      } finally {
        withContext(NonCancellable) {
          logger.info("I'm running finally")
          delay(1000L)
          logger.info("And I've just delayed for 1 sec because I'm non-cancellable")
        }
      }
    }

    delay(1300L) // delay a bit
    logger.info("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    logger.info("main: Now I can quit.")
  }

  companion object {
    val logger: Logger = LoggerFactory.getLogger(CoroutinesDemo::class.qualifiedName)
  }
}
