package com.cedarsoft.common.time

/**
 * JVM specific implementation for the clock now provider
 */
actual object ClockNowProvider : NowProvider {
  actual override fun nowMillis(): Double {
    return System.currentTimeMillis().toDouble()
  }
}
