package it.neckar.open.test.utils

import org.junit.jupiter.api.Test

/**
 */
internal class DisableWhenHeadlessConditionTest {
  @DisableIfHeadless
  @Test
  @Throws(Exception::class)
  fun testItIfHeadless() {
    println("Running")
  }
}
