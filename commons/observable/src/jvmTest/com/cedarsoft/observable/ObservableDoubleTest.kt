package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

internal class ObservableDoubleTest {
  @Test
  internal fun testTypes() {
    val observable = ObservableDouble(12.0)
    val observableReadOnly: ReadOnlyObservableDouble = ObservableDouble(12.0)

    assertThat(observable.get()).isEqualTo(12.0)
    assertThat(observableReadOnly.value).isEqualTo(12.0)
  }

  @Test
  fun testBindingIsEqual() {
    val observable = ObservableDouble(12.0)

    val isEqualTo = observable.isEqualTo(56.0)
    assertThat(isEqualTo.value).isFalse()

    var updated: Boolean? = null
    isEqualTo.consume {
      updated = it
    }
    assertThat(updated).isNull()

    observable.value = 21.0
    assertThat(isEqualTo.value).isFalse()

    observable.value = 56.0
    assertThat(updated).isEqualTo(true)
    assertThat(isEqualTo.value).isTrue()
  }
}
