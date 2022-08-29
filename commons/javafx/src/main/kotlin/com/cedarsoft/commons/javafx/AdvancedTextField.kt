package com.cedarsoft.commons.javafx

import com.cedarsoft.unit.si.ms
import javafx.beans.property.BooleanProperty
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import javafx.util.StringConverter
import javafx.util.converter.DefaultStringConverter

/**
 * A text field that is bound to a value but is not updated while it has the focus
 */
class AdvancedTextField : StackPane() {
  val textField: TextField

  /**
   * A handler that is executed on enter or focus lost
   */
  fun addCommitHandler(commitHandler: CommitHandler) {
    textField.focusedProperty().addListener { observable: ObservableValue<out Boolean?>?, oldValue: Boolean?, newValue: Boolean? -> commitHandler.commit(this@AdvancedTextField, textField.text) }
    textField.onAction = EventHandler { _: ActionEvent -> commitHandler.commit(this@AdvancedTextField, textField.text) }
  }

  fun bindTextBidirectional(property: StringProperty) {
    bindTextBidirectional(property, DefaultStringConverter())
  }

  /**
   * The timestamp of the last relevant interaction (input / focus change)
   */
  private var lastChange: @ms Long = -1

  init {
    textField = TextField()
    children.add(textField)
  }

  private fun updateLastChange(reason: String) {
    lastChange = System.currentTimeMillis()
  }

  fun <T> bindTextBidirectional(property: Property<T>, converter: StringConverter<T>) {
    //Commit the text on enter
    textField.onAction = EventHandler { event: ActionEvent? ->
      property.value = converter.fromString(textField.text)
      lastChange = -1 //reset the last change to ensure updates are reflected
    }

    //Fill the text field initially
    textField.text = converter.toString(property.value)

    //Remember the last time a key has been pressed
    textField.caretPositionProperty().addListener { observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? -> updateLastChange("caret position changed") }
    textField.onKeyPressedProperty().addListener { observable: ObservableValue<out EventHandler<in KeyEvent?>?>?, oldValue: EventHandler<in KeyEvent?>?, newValue: EventHandler<in KeyEvent?>? -> updateLastChange("key pressed") }
    textField.focusedProperty().addListener { observable: ObservableValue<out Boolean?>?, oldValue: Boolean?, newValue: Boolean? -> updateLastChange("focus changed") }
    textField.selectedTextProperty().addListener { observable: ObservableValue<out String?>?, oldValue: String?, newValue: String? -> updateLastChange("selected text changed") }
    property.addListener { observable: ObservableValue<out T>?, oldValue: T, newValue: T ->
      if (textField.isFocused) {
        //Field is focused
        val timeSinceLastInput: @ms Long = System.currentTimeMillis() - lastChange
        if (timeSinceLastInput < 3000) {
          //Only overwrite if the last edit action is at least 1 sec old
          return@addListener
        }
      }
      textField.text = converter.toString(newValue)
    }

    //Update the property when the text field changes
    textField.textProperty().addListener { observable: ObservableValue<out String?>?, oldValue: String?, newValue: String? -> property.setValue(converter.fromString(newValue)) }

    //Update the text field on focus lost
    textField.focusedProperty().addListener { observable: ObservableValue<out Boolean?>?, oldValue: Boolean?, newValue: Boolean? ->
      if (!newValue!!) {
        textField.text = converter.toString(property.value)
      }
    }
  }

  fun editableProperty(): BooleanProperty {
    return textField.editableProperty()
  }

  fun setPrefColumnCount(value: Int) {
    textField.prefColumnCount = value
  }

  /**
   * Handler that is called when the text field is committed
   */
  fun interface CommitHandler {
    fun commit(advancedTextField: AdvancedTextField, text: String)
  }
}
