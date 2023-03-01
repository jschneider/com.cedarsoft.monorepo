package it.neckar.open.javafx.combo

import it.neckar.open.javafx.properties.*
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback

/**
 * Supports a combo box with optional items (that can be disabled)
 */


/**
 * Wraps an option for a combo box that can be disabled
 */
class ItemThatMayBeDisabled<T>(
  /**
   * The value
   */
  val value: T
) {

  /**
   * The disabled property
   */
  val disabledProperty: BooleanProperty = SimpleBooleanProperty()
  var disabled: Boolean by disabledProperty

  override fun toString(): String {
    return super.toString()
  }
}


/**
 * Callback that provides a ListCell that visualizes the disabled state
 */
class ListViewListCellCallbackForItemThatMayBeDisabled<T>(val converter: (T) -> String) : Callback<ListView<ItemThatMayBeDisabled<T>?>, ListCell<ItemThatMayBeDisabled<T>>> {
  override fun call(param: ListView<ItemThatMayBeDisabled<T>?>): ListCell<ItemThatMayBeDisabled<T>> {
    return object : ListCell<ItemThatMayBeDisabled<T>>() {
      override fun updateItem(item: ItemThatMayBeDisabled<T>?, empty: Boolean) {
        super.updateItem(item, empty)

        if (item != null) {
          setText(converter(item.value))
        } else {
          setText("")
        }

        if (item != null && item.disabled) {
          isDisable = true
          setStyle("-fx-opacity: 0.7;")
        } else {
          isDisable = false
          setStyle("")
        }
      }
    }
  }
}
