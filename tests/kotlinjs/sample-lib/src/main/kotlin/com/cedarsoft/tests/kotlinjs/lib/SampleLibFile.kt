package com.cedarsoft.tests.kotlinjs.lib

fun helloSampleWorld(): Int {
  return 42
}

fun getTheTime(): String {
  return "Time: ${currentTimeMillis()}"
}

expect fun currentTimeMillis(): Long
