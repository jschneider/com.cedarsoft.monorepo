package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.*
import org.junit.jupiter.api.Test

class WeakSetTest {
  @Test
  fun testWeakSet() {
    val weakSet = WeakSet<SetElement>()

    SetElement(1).let {
      weakSet.add(it)
      assertThat(weakSet.contains(it))
    }
    assertThat(weakSet.size).isEqualTo(1)

    SetElement(2).let {
      weakSet.add(it)
      assertThat(weakSet.contains(it))
    }
    assertThat(weakSet.size).isEqualTo(2)

    SetElement(3).let {
      weakSet.add(it)
      assertThat(weakSet.contains(it))
    }
    assertThat(weakSet.size).isEqualTo(3)

    assertThat(weakSet.remove(SetElement(1))).isTrue()
    assertThat(weakSet.remove(SetElement(2))).isTrue()
    assertThat(weakSet.remove(SetElement(3))).isTrue()

    assertThat(weakSet.isEmpty())
    assertThat(weakSet.size).isEqualTo(0)
  }

  @Test
  fun testWeakSetGc() {
    val weakSet = WeakSet<SetElement>()

    weakSet.add(SetElement(1))
    weakSet.add(SetElement(2))
    weakSet.add(SetElement(3))

    forceGc {
      !weakSet.contains(SetElement(1)) &&
        !weakSet.contains(SetElement(2)) &&
        !weakSet.contains(SetElement(3))
    }

    //Ensure no elements are left
    assertThat(weakSet.isEmpty())
  }

  data class SetElement(val id: Int)
}

