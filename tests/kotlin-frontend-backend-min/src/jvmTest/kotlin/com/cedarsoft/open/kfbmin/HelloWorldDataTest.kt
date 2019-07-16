package com.cedarsoft.open.kfbmin

import assertk.all
import assertk.assertThat
import assertk.assertions.hasLength
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.startsWith
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class HelloWorldDataTest {
  @Test
  internal fun testHelloWorldInJvm() {
    assertThat { HelloWorldData("daMessage").message }
      .returnedValue {
        isEqualTo("daMessage")
        isNotEqualTo("daMessage222")
      }
  }

  @Test
  fun testSimpleSyntax() {
    val data = HelloWorldData("daMessage")

    assertThat(data.message).isEqualTo("daMessage")
    assertThat(data::message).isEqualTo("daMessage")

    assertThat(data::message).all {
      isEqualTo("daMessage")
      startsWith("daM")
      hasLength(9)
    }
  }
}
