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
 * Calls the function immediately
 */
fun <T> ObservableValue<T>.consumeImmediately(function: (T) -> Unit) {
  consume(true, function)
}

/**
 * The given function is called for all new values.
 * Calls the function immediately if [immediately] is set to true
 */
fun <T> ObservableValue<T>.consume(immediately: Boolean = false, function: (T) -> Unit) {
  addListener { _, _, newValue ->
    function(newValue)
  }

  if (immediately) {
    function(value)
  }
}


/**
 * Creates a binding with a map function
 */
fun <T, R> ObservableValue<T>.map(conversion: (T) -> R): ObservableValue<R> {
  return Bindings.createObjectBinding(Callable {
    conversion(this.value)
  }, this)
}
