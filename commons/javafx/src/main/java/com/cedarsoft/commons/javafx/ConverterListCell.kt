package com.cedarsoft.commons.javafx

import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView

/**
 * List cell that renders using a converter
 */
class ConverterListCell<T>(
  val converter: (T) -> String
) : ListCell<T>() {

  override fun updateItem(item: T, empty: Boolean) {
    super.updateItem(item, empty)
    if (item != null) {
      text = converter(item)
    }
  }

  companion object {
    fun <T> createFor(comboBox: ComboBox<T>, converter: (T) -> String) {
      comboBox.buttonCell = ConverterListCell(converter)
      comboBox.setCellFactory { param: ListView<T>? -> ConverterListCell(converter) }
    }
  }
}
