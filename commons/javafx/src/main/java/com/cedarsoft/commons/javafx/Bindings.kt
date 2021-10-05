package com.cedarsoft.commons.javafx

import com.cedarsoft.dispose.Disposable
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.binding.StringBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.Callable

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
fun <T> ObservableValue<T>.consumeImmediately(function: (T) -> Unit): Disposable {
  return consume(true, function)
}

/**
 * The given function is called for all new values.
 * Calls the function immediately if [immediately] is set to true
 */
fun <T> ObservableValue<T>.consume(immediately: Boolean = false, function: (T) -> Unit): Disposable {
  val listener: (observable: ObservableValue<out T>, oldValue: T, newValue: T) -> Unit = { _, _, newValue ->
    function(newValue)
  }

  addListener(listener)

  //Call the listener immediately (if requested)
  if (immediately) {
    function(value)
  }

  //Returns a disposable that can be used to unregister the listener
  return Disposable {
    removeListener(listener)
  }
}


/**
 * Creates a binding with a map function
 */
fun <T, R> ObservableValue<T>.map(conversion: (T) -> R): ObjectBinding<R> {
  return Bindings.createObjectBinding({
    conversion(this.value)
  }, this)
}

fun <T> ObservableValue<T>.mapToBool(conversion: (T) -> Boolean): BooleanBinding {
  return Bindings.createBooleanBinding({
    conversion(this.value)
  }, this)
}

/**
 * Creates a binding combining two observable values
 */
fun <T1, T2, R> ObservableValue<T1>.map(other: ObservableValue<T2>, conversion: (T1, T2) -> R): ObjectBinding<R> {
  return Bindings.createObjectBinding({
    conversion(this.value, other.value)
  }, this, other)
}

fun <T1, T2, T3, R> ObservableValue<T1>.map(other1: ObservableValue<T2>, other2: ObservableValue<T3>, conversion: (T1, T2, T3) -> R): ObjectBinding<R> {
  return Bindings.createObjectBinding({
    conversion(this.value, other1.value, other2.value)
  }, this, other1, other2)
}

fun <T1, T2, T3, T4, R> ObservableValue<T1>.map(other1: ObservableValue<T2>, other2: ObservableValue<T3>, other3: ObservableValue<T4>, conversion: (T1, T2, T3, T4) -> R): ObjectBinding<R> {
  return Bindings.createObjectBinding({
    conversion(this.value, other1.value, other2.value, other3.value)
  }, this, other1, other2, other3)
}


/**
 * Returns a boolean binding that contains "true" if the value of this property is equal to [expectedValue]
 */
fun <T, R> ObservableValue<T>.isEqualTo(expectedValue: T): BooleanBinding {
  return Bindings.createBooleanBinding(Callable {
    getValue() == expectedValue
  }, this)
}


/**
 * Sets the *next* value.
 *
 * Should only be used for tests
 */
inline fun <reified T : Enum<T>> ObjectProperty<T>.next(values: Array<T> = enumValues()) {
  val currentOrdinal = value.ordinal
  val nextIndex = (currentOrdinal + 1) % values.size

  val nextValue = values[nextIndex]
  this.value = nextValue
}
