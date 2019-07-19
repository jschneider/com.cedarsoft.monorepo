package com.cedarsoft

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun startAsync() {
  println("start")
  GlobalScope.launch {
    println("result = ${concurrentSum()}")
  }
  println("end")
}

suspend fun concurrentSum(): Int = coroutineScope {
  val one = async(CoroutineName("co1")) { doSomethingUsefulOne() }
  val two = async(CoroutineName("co2")) { doSomethingUsefulTwo() }
  one.await() + two.await()
}

suspend fun doSomethingUsefulOne(): Int {
  println("doSomethingUsefulOne")
  delay(5000L) // pretend we are doing something useful here
  return 13
}

suspend fun doSomethingUsefulTwo(): Int {
  println("doSomethingUsefulTwo")
  delay(5000L) // pretend we are doing something useful here, too
  return 29
}

