package it.neckar.open.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

internal class ObservableIntTest {
  @Test
  internal fun testTypes() {
    val observable: ObservableInt = ObservableInt(12)
    val observableReadOnly: ReadOnlyObservableInt = ObservableInt(12)

    assertThat(observable.get()).isEqualTo(12)
    assertThat(observableReadOnly.value).isEqualTo(12)
  }

  @Test
  fun testBindingIsEqual() {
    val observable = ObservableInt(12)

    val isEqualTo = observable.isEqualTo(56)
    assertThat(isEqualTo.value).isFalse()

    var updated: Boolean? = null
    isEqualTo.consume {
      updated = it
    }
    assertThat(updated).isNull()

    observable.value = 21
    assertThat(isEqualTo.value).isFalse()

    observable.value = 56
    assertThat(updated).isEqualTo(true)
    assertThat(isEqualTo.value).isTrue()
  }

}
