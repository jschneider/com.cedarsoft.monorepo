package com.cedarsoft.common.kotlin.lang

/**
 * Contains information about the environment
 */
object ExecutionEnvironment {
  val inUnitTest: Boolean = guessInUnitTestEnvironment()
  val inCI: Boolean = guessInCIEnvironment()
}

/**
 * Returns true if this test is running (probably) in a unit test
 */
expect fun guessInUnitTestEnvironment(): Boolean

/**
 * Returns true if this test is running (probably) in a Continuous Integration environment (e.g. Gitlab CI)
 */
expect fun guessInCIEnvironment(): Boolean
