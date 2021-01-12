package com.cedarsoft.test.utils

import kotlin.system.measureTimeMillis

/**
 * Forces gc until the action returns true.
 * Throws an exception if GC has never been successful
 */
fun forceGc(gcSuccessful: (() -> Boolean)) {
  measureTimeMillis {
    for (i in 1..20) {
      System.gc()

      if (gcSuccessful.invoke()) {
        return
      }
    }
  }.also { println("Took $it ms") }

  throw IllegalStateException("GC not successful")
}


/**
 * Executes the GC
 */
fun gc() {
  measureTimeMillis {
    for (i in 1..20) {
      System.gc()
    }
  }.also { println("Took $it ms") }
}
