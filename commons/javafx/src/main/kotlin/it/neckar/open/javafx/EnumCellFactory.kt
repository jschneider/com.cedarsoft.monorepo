package it.neckar.open.javafx

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback

/**
 * A cell factory that renders enum values using an [EnumTranslator].
 */
class EnumCellFactory<S, T : Enum<*>?> : Callback<TableColumn<S, T>?, TableCell<S, T>> {

  override fun call(param: TableColumn<S, T>?): TableCell<S, T> {
    return object : TableCell<S, T>() {
      override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
          text = null
          setGraphic(null)
        } else {
          text = EnumTranslatorUtil.translate(item)
        }
      }
    }
  }
}
