package com.cedarsoft.commons.javafx

import com.cedarsoft.dispose.Disposable
import com.cedarsoft.formatting.CachedNumberFormat
import com.cedarsoft.formatting.decimalFormat
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.DoubleBinding
import javafx.beans.binding.IntegerBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.binding.StringBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.Callable

fun <T : Enum<T>> ObservableValue<Boolean>.toEnum(valueForTrue: T, valueForFalse: T): ObjectBinding<T> {
  return this.map {
    if (it) {
      valueForTrue
    } else {
      valueForFalse
    }
  }
}

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

fun <T> ObservableValue<T>.mapToInt(conversion: (T) -> Int): IntegerBinding {
  return Bindings.createIntegerBinding({
    conversion(this.value)
  }, this)
}

fun <T1, T2> ObservableValue<T1>.mapToInt(other: ObservableValue<T2>, conversion: (T1, T2) -> Int): IntegerBinding {
  return Bindings.createIntegerBinding({
    conversion(this.value, other.value)
  }, this, other)
}

/**
 * Maps the values of a property to a double
 */
fun <T> ObservableValue<T>.mapToDouble(conversion: (T) -> Double): DoubleBinding {
  return Bindings.createDoubleBinding({
    conversion(this.value)
  }, this)
}

fun <T1, T2> ObservableValue<T1>.mapToDouble(other: ObservableValue<T2>, conversion: (T1, T2) -> Double): DoubleBinding {
  return Bindings.createDoubleBinding({
    conversion(this.value, other.value)
  }, this, other)
}

fun <T1, T2, T3> ObservableValue<T1>.mapToDouble(other1: ObservableValue<T2>, other2: ObservableValue<T3>, conversion: (T1, T2, T3) -> Double): DoubleBinding {
  return Bindings.createDoubleBinding({
    conversion(this.value, other1.value, other2.value)
  }, this, other1, other2)
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
 * Maps a number to a formatted string using the given format
 */
fun ObservableValue<Number>.formatted(format: CachedNumberFormat = decimalFormat): ObjectBinding<String> = map {
  format.format(it.toDouble())
}

/**
 * Formats a nullable property.
 */
fun ObservableValue<Double?>.formattedOrIfNull(valueIfNull: String, format: CachedNumberFormat = decimalFormat): ObjectBinding<String> = map {
  if (it == null) {
    valueIfNull
  } else {
    format.format(it.toDouble())
  }
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


/**
 * Nested  binding for observable value.
 *
 * Can be used like this:
 * ```
 * val name = OuterClass("daName").inner
 *  .select {
 *    it.name
 * }
 *
 * class OuterClass(name: String) {
 *  val inner: ObservableValue<InnerClass> = SimpleObjectProperty(InnerClass())
 * }
 *
 * class InnerClass {
 *  val name: ObservableString = SimpleStringProperty("initial name")
 * }
 * ```
 *
 * The `name` value is updated whenever the `inner` property is changed *and* the `name` property
 * change of the referenced object
 */
fun <T, N> ObservableValue<T>.select(extractNested: (T) -> ObservableValue<N>): ObservableValue<N> {
  //The currently nested value
  var currentNested: ObservableValue<N> = extractNested(value)

  //Holds the nested value
  val nestedObservableObject = SimpleObjectProperty(currentNested.value)

  //Register the value change listener
  val nestedValueListener: (N) -> Unit = { newValue ->
    nestedObservableObject.value = newValue
  }

  var disposable = currentNested.consumeImmediately(nestedValueListener)

  //Update the nested
  consumeImmediately { newValue ->
    //Unregister from the old nested
    disposable.dispose()

    currentNested = extractNested(newValue)
    nestedObservableObject.value = currentNested.value

    disposable = currentNested.consumeImmediately(nestedValueListener)
  }

  return nestedObservableObject
}
