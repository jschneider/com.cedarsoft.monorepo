package com.cedarsoft.swing.binding

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.ComboBoxModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

/**
 * A combo box model that is backed by an observable list
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class ComboBoxObservableListModel<T>() : ComboBoxModel<T> {

  constructor(vararg items: T) : this() {
    this.items.addAll(items)
  }

  constructor(items: Collection<@JvmWildcard T>) : this() {
    this.items.addAll(items)
  }

  /**
   * The items
   */
  val items: ObservableList<T> = FXCollections.observableArrayList<T>()
    .also { items: ObservableList<T> ->
      items.addListener(ListChangeListener {
        for (dataListener in dataListeners) {
          dataListener.contentsChanged(ListDataEvent(this, javax.swing.event.ListDataEvent.CONTENTS_CHANGED, 0, items.size))
        }
      })
    }

  private var selectedItemField: T? = null

  override fun setSelectedItem(anItem: Any?) {
    @Suppress("UNCHECKED_CAST")
    selectedItemField = anItem as T
  }

  override fun getSelectedItem(): T? {
    return selectedItemField
  }

  override fun getElementAt(index: Int): T {
    return items[index]
  }

  override fun getSize(): Int {
    return items.size
  }

  private val dataListeners = CopyOnWriteArrayList<ListDataListener>()

  override fun addListDataListener(l: ListDataListener) {
    dataListeners.add(l)
  }

  override fun removeListDataListener(l: ListDataListener) {
    dataListeners.remove(l)
  }
}
