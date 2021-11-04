package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.BidirectionalBinding.bindBidirectional
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BidirectionalBindingTest {
  @Test
  fun testBuilder() {
    val doubleProperty: DoubleProperty = SimpleDoubleProperty(0.0)
    val stringProperty: StringProperty = SimpleStringProperty("7")

    bindBidirectional(doubleProperty, stringProperty) {
      a2b = { newValue -> newValue.toDouble().toString() }
      b2a = { newValue: String -> newValue.toDouble() }
    }

    assertThat(doubleProperty.doubleValue()).isEqualTo(0.0)
    assertThat(stringProperty.value).isEqualTo("0.0")

    //assign new double
    doubleProperty.value = 17.0
    assertThat(doubleProperty.doubleValue()).isEqualTo(17.0)
    assertThat(stringProperty.value).isEqualTo("17.0")

    //assign string
    stringProperty.value = "22.2"
    assertThat(doubleProperty.doubleValue()).isEqualTo(22.2)
    assertThat(stringProperty.value).isEqualTo("22.2")
  }

  @Test
  fun testWithConverter() {
    val doubleProperty: DoubleProperty = SimpleDoubleProperty(0.0)
    val stringProperty: StringProperty = SimpleStringProperty("7")

    bindBidirectional(doubleProperty,
      stringProperty,
      { number: Number -> number.toDouble().toString() }, { s: String -> s.toDouble() })


    assertThat(doubleProperty.doubleValue()).isEqualTo(0.0)
    assertThat(stringProperty.value).isEqualTo("0.0")

    //assign new double
    doubleProperty.value = 17.0
    assertThat(doubleProperty.doubleValue()).isEqualTo(17.0)
    assertThat(stringProperty.value).isEqualTo("17.0")

    //assign string
    stringProperty.value = "22.2"
    assertThat(doubleProperty.doubleValue()).isEqualTo(22.2)
    assertThat(stringProperty.value).isEqualTo("22.2")
  }

  @Test
  fun bindBidirectional() {
    val lengthCommunication: DoubleProperty = SimpleDoubleProperty(0.0)
    val lengthUI: DoubleProperty = SimpleDoubleProperty(0.0)
    val offsetProperty: DoubleProperty = SimpleDoubleProperty(0.0)

    val updateCounter = intArrayOf(0)

    bindBidirectional(
      lengthCommunication,
      lengthUI,
      { _: ObservableValue<out Number?>?, _: Number?, newValue: Number? ->
        updateCounter[0]++
        org.junit.jupiter.api.Assertions.assertEquals(newValue, lengthCommunication.get())
        lengthUI.value = (lengthCommunication.get() + offsetProperty.get()) / 10
      },
      { _: ObservableValue<out Number?>?, _: Number?, newValue: Number? ->
        updateCounter[0]++
        org.junit.jupiter.api.Assertions.assertEquals(newValue, lengthUI.get())
        lengthCommunication.value = lengthUI.get() * 10 - offsetProperty.get()
      },
      offsetProperty
    )

    assertThat(lengthCommunication.get()).isEqualTo(0.0)
    assertThat(lengthUI.get()).isEqualTo(0.0)

    lengthCommunication.set(10.0)
    assertThat(lengthCommunication.get()).isEqualTo(10.0)
    assertThat(lengthUI.get()).isEqualTo(1.0)

    offsetProperty.value = 20.0
    assertThat(lengthCommunication.get()).isEqualTo(10.0)
    assertThat(lengthUI.get()).isEqualTo(3.0)

    lengthUI.value = 5.0
    assertThat(lengthCommunication.get()).isEqualTo(30.0)
    assertThat(lengthUI.get()).isEqualTo(5.0)
  }
}
