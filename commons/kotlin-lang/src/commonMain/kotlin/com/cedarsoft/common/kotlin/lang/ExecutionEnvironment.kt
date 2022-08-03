package com.cedarsoft.common.kotlin.lang

/**
 * Contains information about the environment
 */
object ExecutionEnvironment {
  val inUnitTest: Boolean = guessInUnitTestEnvironment()
}

/**
 * Returns true if this test is running (probably) in a unit test
 */
expect fun guessInUnitTestEnvironment(): Boolean
