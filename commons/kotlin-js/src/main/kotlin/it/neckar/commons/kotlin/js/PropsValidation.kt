@file:Suppress("FoldInitializerAndIfToElvis", "SENSELESS_COMPARISON")

package it.neckar.commons.kotlin.js

import kotlin.reflect.KClass
import kotlin.reflect.KProperty0

inline fun <reified T : Any> KProperty0<T>.safeGet(): T {
  return safeGet(T::class)
}

/**
 * checks if the passed property is valid (not null, correct instance)
 * and returns it.
 * */
fun <T : Any> KProperty0<T>.safeGet(type: KClass<T>): T {
  val value = this.get()

  if (value == null) {
    throw PropertyValidationFailedException("Property [${this.name}] is not set")
  }

  if ((type.isInstance(value)).not()) {
    throw PropertyValidationFailedException("Property [${this.name}] has invalid value [$value]")
  }

  return value
}

class PropertyValidationFailedException(message: String, cause: Throwable? = null) : Exception(message, cause)



