package it.neckar.open.javafx

import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableValue
import javafx.scene.control.CheckBox
import javax.annotation.Nonnull

/**
 * Helper class that supports a tri state checkbox
 *
 */
class TriStateCheckBox(
  label: String,
  callback: Callback,
  state: ObservableObjectValue<TriStateCheckboxState>,
) {
  val checkBox: CheckBox

  private val callback: Callback

  private val state: ObservableObjectValue<TriStateCheckboxState>

  init {
    checkBox = CheckBox(label)
    //Set selected to true initially to allow an initial indeterminate state
    checkBox.isSelected = true
    this.callback = callback
    this.state = state

    //Update the model when the checkbox has changed
    checkBox.selectedProperty().addListener(object : ChangeListener<Boolean> {
      /**
       * Is set to true during modification to avoid duplicate events
       */
      private var isChanging = false
      override fun changed(observable: ObservableValue<out Boolean>, oldValue: Boolean, newValue: Boolean) {
        if (isChanging) {
          return
        }
        if (newValue) {
          isChanging = true
          try {
            this@TriStateCheckBox.callback.selectAll()
          } finally {
            isChanging = false
          }
        } else {
          try {
            isChanging = true
            this@TriStateCheckBox.callback.selectNone()
          } finally {
            isChanging = false
          }
        }
      }
    })

    //Update the checkbox if the state has changed
    stateProperty().addListener { _: ObservableValue<out TriStateCheckboxState>?, _: TriStateCheckboxState?, newValue: TriStateCheckboxState? -> updateSelectAllCheckbox(checkBox) }
    updateSelectAllCheckbox(checkBox)
  }

  private fun updateSelectAllCheckbox(@Nonnull selectAllCheckBox: CheckBox) {
    when (stateProperty().get()) {
      TriStateCheckboxState.UNSELECTED -> {
        selectAllCheckBox.isIndeterminate = false
        selectAllCheckBox.isSelected = false
        return
      }

      TriStateCheckboxState.INDETERMINATE -> {
        selectAllCheckBox.isIndeterminate = true
        return
      }

      TriStateCheckboxState.SELECTED -> {
        selectAllCheckBox.isIndeterminate = false
        selectAllCheckBox.isSelected = true
        return
      }

      else -> {
        throw IllegalArgumentException("Invalid state <" + stateProperty().get() + ">")
      }
    }
  }

  fun stateProperty(): ObservableObjectValue<TriStateCheckboxState> {
    return state
  }

  /**
   * A model for the tri state checkbox
   */
  interface Callback {
    /**
     * Select all elements
     */
    fun selectAll()

    /**
     * Select no elements
     */
    fun selectNone()
  }

  companion object {
    /**
     * Creates a binding to a tri state checkbox state
     */
    @JvmStatic
    fun createStateProperty(allSelectedProperty: ReadOnlyBooleanProperty, noneSelectedProperty: ReadOnlyBooleanProperty): ObjectBinding<TriStateCheckboxState> {
      return Bindings.createObjectBinding({
        if (allSelectedProperty.get()) {
          return@createObjectBinding TriStateCheckboxState.SELECTED
        }
        if (noneSelectedProperty.get()) {
          return@createObjectBinding TriStateCheckboxState.UNSELECTED
        }
        TriStateCheckboxState.INDETERMINATE
      }, allSelectedProperty, noneSelectedProperty)
    }
  }
}
