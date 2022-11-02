package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 */
class ObservableBindingTest {

  data class MyButtonState(val selected: Boolean, val magicNumber: Int) {
  }

  @Test
  internal fun testBindBooleanBidirectionalManually() {
    val stateProperty: ObservableObject<MyButtonState> = ObservableObject(MyButtonState(false, 77))

    val selectedProperty: ObservableBoolean = ObservableBoolean(false).also { selectedProperty ->
      //Update this property whenever the state property is updated
      stateProperty.consumeImmediately {
        selectedProperty.value = it.selected
      }

      //On update to the selected property write the changes to the state property
      selectedProperty.consume {
        val old = stateProperty.value
        stateProperty.value = old.copy(selected = it)
      }
    }

    verifyBidirectionalBindingWithConversion(stateProperty, selectedProperty)
  }

  @Test
  internal fun testBindBooleanBidirectional() {
    val stateProperty: ObservableObject<MyButtonState> = ObservableObject(MyButtonState(false, 77))
    val selectedProperty: ObservableBoolean = ObservableBoolean(false)

    val converterForward: (newValueToConvert: Boolean, oldConvertedValue: MyButtonState) -> MyButtonState = { selected, oldConvertedValue -> oldConvertedValue.copy(selected = selected) }
    val converterBack: (newValueToConvert: MyButtonState, oldConvertedValue: Boolean) -> Boolean = { newValueToConvert, _ -> newValueToConvert.selected }

    selectedProperty.bindBidirectional(stateProperty, converterForward, converterBack)

    verifyBidirectionalBindingWithConversion(stateProperty, selectedProperty)
  }

  private fun verifyBidirectionalBindingWithConversion(
    stateProperty: ObservableObject<MyButtonState>,
    selectedProperty: ObservableBoolean
  ) {

    assertThat(stateProperty.value.selected).isFalse()
    assertThat(stateProperty.value.magicNumber).isEqualTo(77)
    assertThat(selectedProperty.value).isFalse()

    stateProperty.value = MyButtonState(true, 78)

    assertThat(stateProperty.value.selected).isTrue()
    assertThat(stateProperty.value.magicNumber).isEqualTo(78)
    assertThat(selectedProperty.value).isTrue()

    selectedProperty.value = false

    assertThat(stateProperty.value.selected).isFalse()
    assertThat(stateProperty.value.magicNumber).isEqualTo(78)
    assertThat(selectedProperty.value).isFalse()
  }

  @Test
  internal fun testIt() {
    val observable0 = ObservableObject<String>("a")
    val observable1 = ObservableObject<String>("b")


    val observableLength = ObservableInt(0)
    //observableLength.bind()  //TODO
  }

  /**
   * Bind to a nested value
   */
  @Test
  internal fun testProps() {
    val observable0 = ObservableObject(MyClass("Martha"))
    assertThat(observable0.value.name).isEqualTo("Martha")
    observable0.value.name = "Hans"
    assertThat(observable0.value.name).isEqualTo("Hans")
  }

  @Test
  internal fun testSelect() {
    val outer0 = MyClass("Martha")
    val outer1 = MyClass("Bla")

    val observable0 = ObservableObject(outer0)
    assertThat(observable0.value.name).isEqualTo("Martha")

    val selected: ReadOnlyObservableObject<String> = observable0.select {
      it.nameProperty
    }

    assertThat(selected.value).isEqualTo("Martha")

    //Change the inner value
    outer0.name = "Hans"
    assertThat(selected.value).isEqualTo("Hans")

    //Change the outer value
    observable0.value = outer1
    assertThat(selected.value).isEqualTo("Bla")

    outer1.name = "Fasel"
    assertThat(selected.value).isEqualTo("Fasel")

    //Changing the old outer value --> no changes
    outer0.name = "######"
    assertThat(selected.value).isEqualTo("Fasel") //did not change!
  }

  @Test
  internal fun testNullableValueInner() {
    val outer0 = MyClass("Martha")
    val outer1 = MyClass("Bla")

    val observable0 = ObservableObject(outer0)
    assertThat(observable0.value.nameNullable).isEqualTo("Martha")

    val selected: ReadOnlyObservableObject<String?> = observable0.select {
      it.nameNullableProperty
    }

    assertThat(selected.value).isEqualTo("Martha")

    //Change the inner value
    outer0.nameNullable = "Hans"
    assertThat(selected.value).isEqualTo("Hans")
    outer0.nameNullable = null
    assertThat(selected.value).isNull()

    //Change the outer value
    observable0.value = outer1
    assertThat(selected.value).isEqualTo("Bla")

    outer1.nameNullable = "Fasel"
    assertThat(selected.value).isEqualTo("Fasel")

    //Changing the old outer value --> no changes
    outer0.nameNullable = "######"
    assertThat(selected.value).isEqualTo("Fasel") //did not change!
  }

  @Test
  internal fun testBindingConversion() {
    val observable0 = ObservableObject("1")

    val observableInt = ObservableInt(0)
    assertThat(observableInt.value).isEqualTo(0)

    observableInt.bind(observable0.map {
      Integer.parseInt(it)
    })

    assertThat(observableInt.value).isEqualTo(1)
    observable0.value = "2"
    assertThat(observableInt.value).isEqualTo(2)
  }
}


private class MyClass(name: String) {
  val nameProperty = ObservableObject(name)
  var name by nameProperty

  val nameNullableProperty = ObservableObject<String?>(name)
  var nameNullable by nameNullableProperty


}
