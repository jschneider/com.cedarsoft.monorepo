package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import it.neckar.open.kotlin.lang.consumeUntil
import it.neckar.open.kotlin.lang.consumeWhile
import it.neckar.open.kotlin.lang.getModulo
import it.neckar.open.kotlin.lang.withNullAtFirst
import org.junit.jupiter.api.Test

class CollectionExtensionsKtTest {
  @Test
  fun name() {
    assertThat(listOf(1, 2, 3).withNullAtFirst()).containsExactly(null, 1, 2, 3)
  }

  @Test
  fun testConsumeUntil2() {
    val list = listOf(1, 2, 3, 4, 5)

    val consumeUntil = list.consumeUntil("3") {
      it.toString()
    } ?: "null"


  }

  @Test
  fun testModulo() {
    val elements = listOf("0", "1", "2", "3")

    assertThat(elements.getModulo(0)).isEqualTo("0")
    assertThat(elements.getModulo(1)).isEqualTo("1")
    assertThat(elements.getModulo(2)).isEqualTo("2")
    assertThat(elements.getModulo(3)).isEqualTo("3")
    assertThat(elements.getModulo(4)).isEqualTo("0")
    assertThat(elements.getModulo(400)).isEqualTo("0")
    assertThat(elements.getModulo(401)).isEqualTo("1")

    assertThat(elements.getModulo(-1)).isEqualTo("3")
  }

  @Test
  fun testConsumeWhile() {
    val list = listOf(1, 2, 3, 4, 5)
    var count = 0
    list.consumeWhile {
      if (it < 4) {
        ++count
        true
      } else {
        false
      }
    }
    assertThat(count).isEqualTo(3)
  }

  @Test
  fun testConsumeUntil() {
    val list = listOf(1, 2, 3, 4, 5)
    var count = 0
    list.consumeUntil {
      if (it < 4) {
        ++count
        false
      } else {
        true
      }
    }
    assertThat(count).isEqualTo(3)
  }

}
