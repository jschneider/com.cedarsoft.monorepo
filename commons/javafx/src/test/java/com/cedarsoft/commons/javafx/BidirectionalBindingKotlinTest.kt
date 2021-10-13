package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
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

  @Test
  fun testBinding() {
    val doubleProp = SimpleDoubleProperty(5.0)
    val intProp = SimpleIntegerProperty(105)

    doubleProp.bindBidirectionalWithFactor(intProp, 10.0)

    //initial values
    assertThat(doubleProp.value).isEqualTo(10.5)
    assertThat(intProp.value).isEqualTo(105)

    //set int
    intProp.value = 700
    assertThat(intProp.value).isEqualTo(700)
    assertThat(doubleProp.value).isEqualTo(70.0)

    //set double
    doubleProp.value = 80.0
    assertThat(intProp.value).isEqualTo(800)
    assertThat(doubleProp.value).isEqualTo(80.0)
  }
}
