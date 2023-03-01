package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.*
import org.junit.jupiter.api.Test

class WeakMapTest {
  @Test
  fun testWeakMap() {
    val map = WeakMap<Foo, Bar>()
    addStuff(map)

    forceGc {
      map[Foo(1)] == null
        &&
        map[Foo(2)] == null
        &&
        map[Foo(3)] == null
    }

    //Ensure no keys left
    assertThat(map[Foo(1)]).isNull()
    assertThat(map[Foo(2)]).isNull()
    assertThat(map[Foo(3)]).isNull()
  }

  private fun addStuff(weakMap: WeakMap<Foo, Bar>) {
    Foo(1).also {
      weakMap[it] = Bar("bar1")
      assertThat(weakMap[it]).isNotNull()
    }
    Foo(2).also {
      weakMap[it] = Bar("bar1")
      assertThat(weakMap[it]).isNotNull()
    }
    Foo(3).also {
      weakMap[it] = Bar("bar1")
      assertThat(weakMap[it]).isNotNull()
    }
  }
}

data class Foo(val id: Int) {

}

data class Bar(val name: String) {

}
