package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

/**
 *
 */
class DebounceTest {
  @Test
  fun testThrottleLatest() = runTest {
    val calledValues = mutableListOf<String>()

    val throttleFunction = throttleLatest<String>(100L, this) {
      calledValues.add(it)
    }

    throttleFunction("a")
    throttleFunction("b")

    assertThat(calledValues).isEmpty()
    advanceTimeBy(90)
    assertThat(calledValues).isEmpty()
    advanceTimeBy(50)
    assertThat(calledValues).containsExactly("b")

    throttleFunction("c")
    assertThat(calledValues).containsExactly("b")
    advanceUntilIdle()

    assertThat(calledValues).containsExactly("b", "c")
  }
}
