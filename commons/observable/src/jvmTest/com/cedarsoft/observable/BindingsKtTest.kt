package it.neckar.open.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class BindingsKtTest {
  @Test
  fun testSelect() {
    val fooProperty = ObservableObject(Foo("first"))

    assertThat(fooProperty.value.name.value).isEqualTo("first")
    val selectedName = fooProperty.select {
      it.name
    }

    assertThat(selectedName.value).isEqualTo("first")

    fooProperty.value = Foo("second")
    assertThat(selectedName.value).isEqualTo("second")
    fooProperty.value.name.value = "second"
  }

  @Test
  fun testReduce() {
    val prop1 = ObservableString("1")
    val prop2 = ObservableString("2")

    val reduced = prop1.reduce(prop2) {
      it
    }

    var updated: List<String>? = null
    reduced.consume {
      assertThat(updated).isNull()
      updated = it
    }

    assertThat(updated).isNull()
    prop2.value = "2updated"
    assertThat(updated).isNotNull()
    assertThat(updated!!).containsExactly("1", "2updated")
  }

  private class Foo(initialName: String) {
    val name = ObservableString(initialName)
  }
}
