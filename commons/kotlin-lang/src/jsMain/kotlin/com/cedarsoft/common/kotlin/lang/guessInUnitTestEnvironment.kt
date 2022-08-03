package com.cedarsoft.common.kotlin.lang

/**
 * Returns true if this test is running (probably) in a unit test
 */
actual fun guessInUnitTestEnvironment(): Boolean {
  //No way to know for sure!
  return false
}
