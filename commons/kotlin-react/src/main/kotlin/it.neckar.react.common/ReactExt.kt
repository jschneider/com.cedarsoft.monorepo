package it.neckar.react.common

import it.neckar.react.common.form.*
import kotlinext.js.Object
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.HTMLTag
import kotlinx.html.LABEL
import kotlinx.html.Tag
import kotlinx.html.classes
import react.*
import react.dom.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


/**
 * Shortcut for
 * ```kotlin
 * child(MyComponent::class) {
 *     spread(props) { key, value ->
 *         attrs[key] = value
 *     }
 * }
 * ```
 *
 * Allows to write `<MyComponent ...props/>` as
 * ```kotlin
 * child(MyComponent::class) {
 *     spread(props)
 * }
 * ```
 *
 * @param jsObject a JS object properties of which will be used
 */
fun <T : Tag> RDOMBuilder<T>.spread(jsObject: Any) {
  spread(jsObject) { key, value ->
    attrs[key] = value
  }
}

/**
 * Attempt to mimic `...` operator from ES6.
 * For example, equivalent of `<MyComponent ...props/>` would be
 * ```kotlin
 * child(MyComponent::class) {
 *     spread(props) { key, value ->
 *         attrs[key] = value
 *     }
 * }
 * ```
 *
 * @param jsObject a JS object which properties will be used
 * @param handler a handler for [jsObject]'s property names and values
 */
@Suppress("TYPE_ALIAS")
fun spread(jsObject: Any, handler: (key: String, value: Any) -> Unit) {
  Object.keys(jsObject).map {
    it to jsObject.asDynamic()[it] as Any
  }
    .forEach { (key, value) ->
      handler(key, value)
    }
}

/**
 * Returns the value
 */
val <T> StateInstance<T>.value: T
  get() {
    return this.component1()
  }

val <T> StateInstance<T>.setter: StateSetter<T>
  get() {
    return this.component2()
  }

/**
 * Sets the correct html for fixed
 */
var RDOMBuilder<LABEL>.htmlForFixed: String
  @Deprecated("write only", level = DeprecationLevel.HIDDEN)
  get() {
    return attrs["htmlFor"] as String
  }
  set(value) {
    attrs["htmlFor"] = value
  }


/**
 * Sets the correct html for fixed
 */
var LABEL.htmlForFixed: String
  @Deprecated("write only", level = DeprecationLevel.HIDDEN)
  get() {
    return attributes["htmlFor"] as String
  }
  set(value) {
    attributes["htmlFor"] = value
  }

/**
 * Sets the aria label
 */
var HTMLTag.ariaLabel: String
  @Deprecated("write only", level = DeprecationLevel.HIDDEN)
  get() {
    return attributes["aria-label"] as String
  }
  set(value) {
    attributes["aria-label"] = value
  }

/**
 * Sets the "aria-describedby" attribute
 */
var HTMLTag.ariaDescribedBy: String
  @Deprecated("write only", level = DeprecationLevel.HIDDEN)
  get() {
    return attributes["aria-describedby"] as String
  }
  set(value) {
    attributes["aria-describedby"] = value
  }

/**
 * Appends a new class
 */
fun CommonAttributeGroupFacade.addClass(newClass: String) {
  classes = classes + newClass
}

/**
 * Removes an old class
 */
fun CommonAttributeGroupFacade.removeClass(oldClass: String) {
  classes = classes - oldClass
}

/**
 * Adds the classes - separated by space
 */
fun CommonAttributeGroupFacade.addClasses(newClasses: String) {
  classes = classes + newClasses.split(' ')
}


/**
 * Converts a setter to an onChange listener.
 * Does not set values that can not be parsed!
 */
fun StateInstance<Int>.asOnChangeForInt(numberConstraint: NumberConstraint): (String) -> Unit {
  return useCallback(setter, numberConstraint) {
    if (it.isEmpty()) {
      //setter(0)
      return@useCallback
    }

    it.toIntOrNull()?.let { parsedInt ->
      setter(numberConstraint.constraint(parsedInt))
    }
  }
}

/**
 * Converts a setter to an onChange listener.
 * Does not set values that can not be parsed!
 */
fun StateInstance<Double>.asOnChangeForDouble(numberConstraint: NumberConstraint): (String) -> Unit {
  return useCallback(setter) {
    if (it.isEmpty()) {
      //do *not* set a value, since it is empty
      return@useCallback
    }

    it.toDoubleOrNull()?.let { parsedDouble ->
      setter(numberConstraint.constraint(parsedDouble))
    }
  }
}

fun StateInstance<Boolean>.asOnChangeForBoolean(): (String) -> Unit {
  return useCallback(setter) {
    println("asOnChangeForBoolean called with <$it>")

    if (it.isEmpty()) {
      //do *not* set a value, since it is empty
      return@useCallback
    }

    setter(it.toBoolean())
  }
}

/**
 * Helper function that ensures the same callback is always added.
 *
 * Must only be called from a functional component
 */
fun ensureSameCallback(currentCallback: Any, contextInformation: String) {
  val refToLastCallback = useRef<Any>()

  //Disable for now
  if (true) {
    return
  }

  val last = refToLastCallback.current
  if (last != null) {
    if (last != currentCallback) {
      console.warn("Callback change detected! in <$contextInformation>")
      console.warn("Was: ")
      console.dir(last)
      console.warn("But now:")
      console.dir(currentCallback)
    }
  }

  //Assign the current value
  refToLastCallback.current = currentCallback
}

/**
 * Adds the "invalid" class if the provided invalid check fails
 */
inline fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.invalidIf(invalidCheck: () -> Boolean) {
  contract {
    callsInPlace(invalidCheck, InvocationKind.EXACTLY_ONCE)
  }

  addClassIf("invalid", invalidCheck)
}

/**
 * Adds the "valid" class if the provided invalid check fails
 */
inline fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.validIf(invalidCheck: () -> Boolean) {
  contract {
    callsInPlace(invalidCheck, InvocationKind.EXACTLY_ONCE)
  }

  addClassIf("is-valid", invalidCheck)
}

/**
 * Adds the given class if the given check returns true
 */
inline fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.addClassIf(className: String, check: () -> Boolean?) {
  if (check() == true) {
    attrs {
      addClass(className)
    }
  }
}

/**
 * Removes the given class if the given check returns true
 */
inline fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.removeClassIf(className: String, check: () -> Boolean?) {
  if (check() == true) {
    attrs {
      removeClass(className)
    }
  }
}


/**
 * Returns this if in the list of available options, else returns the first element of the list.
 *
 * This can be used to automatically update a value to the first entry of a select list
 */
fun <T> T?.orFirstIfNotInListOrNull(available: List<T>): T {
  if (this != null && available.contains(this)) {
    return this
  }

  return available.first()
}


/**
 * Must only be called inside a [fc].
 *
 * Checks the dependencies using *==*.
 * Attention. This method *might* be slow(er)
 */
inline fun <T : Any> useMemoCompare(
  vararg dependencies: Any,
  noinline callback: () -> T,
): T {

  /**
   * Reference to the previous value
   */
  val previousDepsRef = useRef<Array<*>>()

  /**
   * Reference to the previous value
   */
  val previousValueRef = useRef<T>()

  val previousDeps = previousDepsRef.current
  val previousValue = previousValueRef.current


  if (previousValue != null && previousDeps.contentEquals(dependencies)) {
    //previous value should be finde
    return previousValue
  }

  //previous value has to be recalculated, since the deps have been updated
  return callback().also { updatedValue ->
    previousDepsRef.current = dependencies
    previousValueRef.current = updatedValue
  }
}
