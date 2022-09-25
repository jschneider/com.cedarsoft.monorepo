package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import kotlin.test.Test

class ExecutionEnvironmentTest {
  @Test
  fun testExecutionEnvironment() {
    assertThat(ExecutionEnvironment.environmentMode).isEqualTo(EnvironmentMode.Dev)
  }
}
