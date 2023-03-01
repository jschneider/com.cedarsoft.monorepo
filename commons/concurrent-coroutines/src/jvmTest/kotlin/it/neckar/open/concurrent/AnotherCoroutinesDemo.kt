package it.neckar.open.concurrent

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

fun main(): Unit = runBlocking(CoroutineName("MyCoroutineName") + Dispatchers.Default) {
  println("Starting demo in ${Thread.currentThread().name}")


  val eventFlow = scheduleEventFlow()


  val collectJob1 = launch {
    eventFlow.onEach {
      println("1 Received $it in ${Thread.currentThread().name}")
    }.collect()
  }

  val collecting = mutableListOf<String>()

  val collectJob2 = eventFlow.onEach {
    println("2 Received $it in ${Thread.currentThread().name}")
    collecting.add(it)
  }.launchIn(this)


  delay(400)
  collectJob1.cancel()

  delay(999)
  println("Stopping emitter")

  collectJob2.cancel()
  println("Collected ${collecting.size} elements")

  measureTimeMillis {
    collectJob1.cancelAndJoin()
  }.also { println("Stopped collectJob after $it ms") }


  //
  //
  // Cleanup!
  //
  //

  measureTimeMillis {
    emitterJob.cancelAndJoin()
  }.also { println("Stopped emitterJob after $it ms") }

  println("Done!")
}


lateinit var emitterJob: Job

/**
 * Simulates a event source
 */
fun CoroutineScope.scheduleEventFlow(): SharedFlow<String> {
  val mutableSharedFlow = MutableSharedFlow<String>(2)

  emitterJob = launch(Dispatchers.IO) {
    var counter = 0
    while (isActive) {
      val value = "Entry $counter"
      println("Emitting $value")
      mutableSharedFlow.emit(value)
      counter++
      delay(100)
    }
  }

  return mutableSharedFlow.asSharedFlow()
}


fun createFlow(): Flow<String> {
  return flow {
    println("in string")

    var counter = 0

    while (currentCoroutineContext().isActive) {
      this.emit("NewValue $counter")
      counter++
      delay(100)
    }
  }
}

