package com.cedarsoft.commons.javafx

import javafx.beans.property.SimpleBooleanProperty
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class OverrideBindingTest {
  @Test
  fun testOverrideBinding() {
    val myProperty = SimpleBooleanProperty()
    val aProperty = SimpleBooleanProperty()
    val bProperty = SimpleBooleanProperty()
    Assertions.assertThat(myProperty.get()).isFalse
    Assertions.assertThat(aProperty.get()).isFalse
    Assertions.assertThat(bProperty.get()).isFalse
    myProperty.bind(aProperty)
    Assertions.assertThat(myProperty.get()).isFalse
    myProperty.bind(bProperty)
    Assertions.assertThat(myProperty.get()).isFalse
    aProperty.set(true)
    Assertions.assertThat(myProperty.get()).isFalse
    bProperty.set(true)
    Assertions.assertThat(myProperty.get()).isTrue
  }
}
