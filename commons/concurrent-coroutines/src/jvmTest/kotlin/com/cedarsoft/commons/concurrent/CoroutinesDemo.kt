package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*


fun main(): Unit = runBlocking {
  val job = launch {
    printWorld()
  }

  println("Hello")
  println("job.isActive: ${job.isActive}")
  println("job.isCancelled: ${job.isCancelled}")
  println("job.isCompleted: ${job.isCompleted}")

  job.cancel("Stop it!")
  job.join()

  println("job.isActive: ${job.isActive}")
  println("job.isCancelled: ${job.isCancelled}")
  println("job.isCompleted: ${job.isCompleted}")

}

private suspend fun printWorld() {
  coroutineScope {
    launch {
      delay(300)
      println(" World!")
    }
  }
}

