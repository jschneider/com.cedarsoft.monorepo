package com.cedarsoft.commons.provider

import kotlin.reflect.KProperty0

/**
 * Provides a *single* double - to avoid boxing
 */
fun interface DoubleProvider {
  /**
   * Provides the double
   */
  operator fun invoke(): Double
}

/**
 * Wraps the given double in a DoubleProvider
 */
fun Double.asDoubleProvider(): DoubleProvider {
  return DoubleProvider { this }
}

/**
 * Returns a delegate that uses the current value of this property to delegate all calls.
 */
fun KProperty0<DoubleProvider>.delegate(): DoubleProvider {
  return DoubleProvider {
    get().invoke()
  }
}
