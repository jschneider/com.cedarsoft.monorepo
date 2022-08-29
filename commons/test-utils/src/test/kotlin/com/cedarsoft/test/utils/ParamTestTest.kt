package com.cedarsoft.test.utils

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParamTestTest {
  @ParameterizedTest
  @MethodSource("dataProvider")
  fun daTest(candidate: String, value: Int) {
    Assertions.assertThat(candidate.toInt()).isEqualTo(value)
  }

  fun dataProvider(): Array<Arguments> {
    return arrayOf(
      Arguments.of("1", 1),
      Arguments.of("2", 2),
      Arguments.of("3", 3)
    )
  }
}
