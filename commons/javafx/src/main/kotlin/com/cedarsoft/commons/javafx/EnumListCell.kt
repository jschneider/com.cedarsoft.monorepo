package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.EnumTranslatorUtil.enumTranslator
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import javax.annotation.Nonnull

/**
 * List cell that renders an enum
 */
class EnumListCell<T : Enum<T>> : ListCell<T>() {
  override fun updateItem(item: T?, empty: Boolean) {
    super.updateItem(item, empty)
    if (item != null) {
      text = enumTranslator.translate(item)
    }
  }

  fun apply(@Nonnull comboBox: ComboBox<T>) {
    comboBox.buttonCell = this
    comboBox.cellFactory = Callback { param: ListView<T>? -> EnumListCell() }
  }

  companion object {
    @JvmStatic
    fun <T : Enum<T>> createFor(@Nonnull comboBox: ComboBox<T>?) {
      EnumListCell<T>().apply(comboBox!!)
    }
  }
}
