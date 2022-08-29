package com.cedarsoft.commons.javafx

import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell

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
    @JvmStatic
    fun <T> createFor(comboBox: ComboBox<T>, converter: (T) -> String) {
      comboBox.buttonCell = ConverterListCell(converter)
      comboBox.setCellFactory { ConverterListCell(converter) }
    }
  }
}

/**
 * Registers the converter
 */
fun <T> ComboBox<T>.formatUsingConverter(converter: (T) -> String) {
  buttonCell = ConverterListCell(converter)
  setCellFactory { ConverterListCell(converter) }
}
