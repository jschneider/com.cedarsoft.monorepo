package it.neckar.open.collections

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ArrayExtensionsKtTest {

  @Test
  fun testModulo() {
    val elements = arrayOf("0", "1", "2", "3")

    Assertions.assertThat(elements.getModulo(0)).isEqualTo("0")
    Assertions.assertThat(elements.getModulo(1)).isEqualTo("1")
    Assertions.assertThat(elements.getModulo(2)).isEqualTo("2")
    Assertions.assertThat(elements.getModulo(3)).isEqualTo("3")
    Assertions.assertThat(elements.getModulo(4)).isEqualTo("0")
    Assertions.assertThat(elements.getModulo(400)).isEqualTo("0")
    Assertions.assertThat(elements.getModulo(401)).isEqualTo("1")

    Assertions.assertThat(elements.getModulo(-1)).isEqualTo("3")
  }
}
