package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.junit.jupiter.api.Test

class BidirectionalBindingKotlinTest {
  @Test
  fun testBooleanEnum() {
    val booleanProperty = SimpleBooleanProperty()
    val enumProperty = SimpleObjectProperty(MyEnum.True)

    BidirectionalBinding.bindBidirectional(booleanProperty, enumProperty, MyEnum.True, MyEnum.False)

    //Copied from A to B
    assertThat(booleanProperty.get()).isEqualTo(false)
    assertThat(enumProperty.get()).isEqualTo(MyEnum.False)

    booleanProperty.set(true)
    assertThat(booleanProperty.get()).isEqualTo(true)
    assertThat(enumProperty.get()).isEqualTo(MyEnum.True)

    booleanProperty.set(false)
    assertThat(booleanProperty.get()).isEqualTo(false)
    assertThat(enumProperty.get()).isEqualTo(MyEnum.False)


    //Changing B
    enumProperty.set(MyEnum.True)
    assertThat(booleanProperty.get()).isEqualTo(true)
    assertThat(enumProperty.get()).isEqualTo(MyEnum.True)

    enumProperty.set(MyEnum.False)
    assertThat(booleanProperty.get()).isEqualTo(false)
    assertThat(enumProperty.get()).isEqualTo(MyEnum.False)
  }

  internal enum class MyEnum {
    True,
    False
  }
}
