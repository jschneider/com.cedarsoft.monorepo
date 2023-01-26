package com.cedarsoft.commons

import com.cedarsoft.unit.si.ms
import com.cedarsoft.unit.si.ns
import com.google.errorprone.annotations.CheckReturnValue
import java.util.concurrent.TimeUnit

/**
 * Simple stopwatch
 */
object Stopwatch {
  @CheckReturnValue
  @ms
  inline fun measureLong(callback: () -> Unit): Long {
    @ms val start = System.currentTimeMillis()
    callback()
    return System.currentTimeMillis() - start
  }

  @CheckReturnValue
  @ns
  inline fun measureNanos(callback: () -> Unit): Long {
    @ns val start = System.nanoTime()
    callback()
    return System.nanoTime() - start
  }


  @CheckReturnValue
  inline fun measure(callback: () -> Unit): Result {
    @ns val start = System.nanoTime()
    callback()
    return Result(System.nanoTime() - start)
  }
}

data class Result(val ns: Long) {
  @CheckReturnValue
  fun format(): String {
    return "Took ${TimeUnit.NANOSECONDS.toMillis(ns)} ms"
  }
}

