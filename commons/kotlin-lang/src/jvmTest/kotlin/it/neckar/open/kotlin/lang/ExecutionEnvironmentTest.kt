package it.neckar.open.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class ExecutionEnvironmentTest {
  @Test
  fun testInEnvironment() {
    assertThat(ExecutionEnvironment.inUnitTest).isTrue()
  }
}
