package com.cedarsoft.commons

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Checks that the compiler is configured to add the parameter names to the byte code.
 * Necessary to avoid clumsy Jackson annotations in constructors
 */
class KotlinJavaParamNamesTest {
  @Test
  internal fun testKotlinParamName() {
    val constructors = AnotherClass::class.constructors
    assertThat(constructors).hasSize(1)

    val constructor = constructors.first()
    assertThat(constructor.parameters).hasSize(1)

    assertThat(constructor.parameters.first().name).isEqualTo("blaBlub")
  }

  @Test
  internal fun testJavaParams() {
    val constructors = AnotherClass::class.java.constructors
    assertThat(constructors).hasSize(1)

    val constructor = constructors.first()
    assertThat(constructor.parameters).hasSize(1)

    assertThat(constructor.parameters.first().name).isEqualTo("blaBlub")
  }
}


private class AnotherClass(private val blaBlub: String) {
}
