package com.cedarsoft.common.kotlin.lang

/**
 * Returns true if this test is running (probably) in a unit test
 */
actual fun guessInUnitTestEnvironment(): Boolean {
  //No way to know for sure!
  return false
}

/**
 * Returns true if this test is running (probably) in a Continuous Integration environment (e.g. Gitlab CI)
 */
actual fun guessInCIEnvironment(): Boolean {
  //no way to know for sure!
  return false
}
