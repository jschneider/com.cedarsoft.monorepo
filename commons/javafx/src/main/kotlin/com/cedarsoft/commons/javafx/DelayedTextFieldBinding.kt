package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.converter.String2StringConverter
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.StringConverter

/**
 * Binds a property to a text field that is only updated on focus lost or enter
 */
class DelayedTextFieldBinding<T>(
  val textField: TextField,
  val property: Property<T>,
  converter: StringConverter<T>,
) {
  val converter: StringConverter<T>

  private val text2fieldListener: ChangeListener<T>

  private val focusLostListener: ChangeListener<Boolean?>

  /**
   * Constructor
   *
   * @param textField the target text field
   * @param property  the property the text field's text property is bidirectionally bound to
   * @param converter its `toString`-method is called to convert the value of the property to the text of the text field;
   * its `fromString`-method is called to convert the text of the text field to the value of the property.
   */
  init {
    check(!property.isBound) { "property must not be bound" }
    this.converter = converter

    //Set initial value
    updateTextField()

    //Update text field on property change
    text2fieldListener = ChangeListener { _: ObservableValue<out T>?, _: T, newValue: T ->
      if (textField.text == newValue) {
        return@ChangeListener
      }
      updateTextField()
    }
    property.addListener(text2fieldListener)

    //Update the property on focus lost
    focusLostListener = ChangeListener { _: ObservableValue<out Boolean?>?, _: Boolean?, newValue: Boolean? ->
      if (!newValue!!) {
        updateProperty()
      }
    }
    textField.focusedProperty().addListener(focusLostListener)

    //Update the property on enter
    val enterPressedListener = EventHandler { event: KeyEvent ->
      if (event.code == KeyCode.ENTER) {
        updateProperty()
      }
    }
    textField.onKeyPressedProperty().value = enterPressedListener
  }

  private fun updateTextField() {
    textField.text = converter.toString(property.value)
  }

  /**
   * Writes the value from the text field to the property
   */
  private fun updateProperty() {
    property.value = converter.fromString(textField.text)
  }

  /**
   * Unbinds everything
   */
  fun unbind() {
    property.removeListener(text2fieldListener)
    textField.focusedProperty().removeListener(focusLostListener)
    textField.onKeyPressedProperty().value = null
  }

  companion object {
    @JvmStatic
    fun connect(textField: TextField, text: Property<String>): DelayedTextFieldBinding<String> {
      return DelayedTextFieldBinding(textField, text, String2StringConverter())
    }

    /**
     * Creates a delayed text field binding.
     *
     * @param textField the target text field
     * @param text      the property the text field's text property is bidirectionally bound to
     * @param converter its `toString`-method is called to convert the value of the property to the text of the text field;
     * its `fromString`-method is called to convert the text of the text field to the value of the property.
     */
    @JvmStatic
    fun <T> connect(textField: TextField, text: Property<T>, converter: StringConverter<T>): DelayedTextFieldBinding<T> {
      return DelayedTextFieldBinding(textField, text, converter)
    }
  }
}
