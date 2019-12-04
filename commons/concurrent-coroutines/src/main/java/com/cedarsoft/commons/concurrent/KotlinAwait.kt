package com.cedarsoft.commons.concurrent

import com.cedarsoft.unit.si.ms
import kotlinx.coroutines.*
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object KotlinAwait {

  suspend fun untilAtomicIsTrue(atomic: AtomicBoolean, @ms maxWaitTime: Long = 10_000, @ms checkDelay: Long = 100) {
    until(maxWaitTime, checkDelay) {
      atomic.get()
    }
  }

  suspend fun until(@ms maxWaitTime: Long = 10_000, @ms checkDelay: Long = 100, function: () -> Boolean) {
    val start = System.currentTimeMillis()

    while (!function()) {
      @ms val waitTime = System.currentTimeMillis() - start
      if (waitTime > maxWaitTime) {
        throw TimeoutException("Atomic boolean did not become true. Waited for $waitTime ms")
      }

      //Wait until the next check
      delay(checkDelay)
    }

  }
}
