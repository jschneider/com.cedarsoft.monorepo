package com.cedarsoft.commons.javafx

import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.Callable

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */


/**
 * Returns a string binding with a max length for a string property
 */
fun StringProperty.maxLength(maxLength: Int): StringBinding {
  return Bindings.createStringBinding(Callable {
    val string = this.get()

    return@Callable StringUtils.abbreviate(string, maxLength)
  }, this)
}

/**
 * Registers a list change listener
 */
fun <T> ObservableList<T>.onListChange(listener: (ListChangeListener.Change<out T>) -> Unit) {
  this.addListener(ListChangeListener {
    listener(it)
  })
}

/**
 * The given function is called for the current value and all new values
 */
fun <T> ObservableValue<T>.consume(function: (T) -> Unit) {
  function(value)
  addListener { _, _, newValue ->
    function(newValue)
  }
}
