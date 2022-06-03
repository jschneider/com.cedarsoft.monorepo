package com.cedarsoft.commons.tornadofx

import javafx.collections.ListChangeListener
import javafx.scene.control.TableView

/**
 */

/**
 * Selects the first added element
 */
fun <T> TableView<T>.selectNewElementsOnAdd() {
  items.addListener(ListChangeListener {
    while (it.next()) {
      if (!it.addedSubList.isEmpty()) {
        val element = it.addedSubList.get(0)
        selectionModel.select(element)
        return@ListChangeListener
      }
    }
  })
}
