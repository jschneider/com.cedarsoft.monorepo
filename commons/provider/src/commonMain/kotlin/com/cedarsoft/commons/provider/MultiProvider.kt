package com.cedarsoft.commons.provider

import com.cedarsoft.common.kotlin.lang.getModulo
import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty0

/**
 * Returns values for a given index.
 * Does *not* have a size. In most cases it is helpful to use a [SizedProvider] instead
 *
 * ATTENTION: Do not replace this interface with lambdas. Lambdas do not support primitive types (at least in JVM 1.8).
 * Therefore, the index is always boxed!
 *
 * @param IndexContext: The type for the index.
 * This should be either:
 * - a value class wrapping an index
 * - an annotation annotated with [MultiProviderIndexContextAnnotation] - if a value class seems to be overkill
 * To avoid boxing this value is *not* used as parameter for [valueAt] (but should in theory).
 *
 * Please create extension methods for [valueAt] that take the value class as parameter and delegate accordingly.
 */
fun interface MultiProvider<in IndexContext, out T> {
  /**
   * Retrieves the value at the given [index].
   *
   * Please use extension methods with the correct type instead (if possible)
   */
  fun valueAt(index: Int): T

  companion object {
    /**
     * Wraps the given list into a [MultiProvider].
     * Does *NOT* support empty lists
     */
    @JvmStatic
    fun <IndexContext, T> forListModulo(values: List<T>): MultiProvider<IndexContext, T> {
      return MultiProvider { index -> values.getModulo(index) }
    }

    @JvmStatic
    fun <IndexContext, T> modulo(vararg values: T): MultiProvider<IndexContext, T> {
      return forListModulo(values.toList())
    }

    /**
     * Wraps the given list into a [MultiProvider].
     * Returns the fallback value if the given [values] list is empty
     */
    @JvmStatic
    fun <IndexContext, T> forListModulo(values: List<T>, fallback: T): MultiProvider<IndexContext, T> {
      if (values.isEmpty()) {
        return always(fallback)
      }

      return forListModulo(values)
    }

    /**
     * Returns the values from the list - and null for all indices that are not contained in the given list.
     * Supports empty lists
     */
    @JvmStatic
    fun <IndexContext, T> forListOrNull(values: List<T>): MultiProvider<IndexContext, T?> {
      return MultiProvider { index -> values.getOrNull(index) }
    }

    /**
     * Returns the values from the list - and the fallback for all indices that are not contained in the given list.
     * Supports empty lists
     */
    @JvmStatic
    fun <IndexContext, T> forListOr(values: List<T>, fallback: T): MultiProvider<IndexContext, T> {
      return MultiProvider { index -> values.getOrNull(index) ?: fallback }
    }

    /**
     * Returns the values of the list or throws an exception if there is no element in the list at the requested index.
     */
    @JvmStatic
    fun <IndexContext, T> forListOrException(values: List<T>): MultiProvider<IndexContext, T> {
      return MultiProvider { index -> values[index] }
    }

    /**
     * Always returns the given value - for all indices
     */
    @JvmStatic
    fun <IndexContext, T> always(value: T): MultiProvider<IndexContext, T> {
      return MultiProvider { value }
    }

    /**
     * Throws an exception if called.
     * This object should only be used if it is ensured that [valueAt] is not called.
     */
    @JvmStatic
    fun <IndexContext, T> empty(): MultiProvider<IndexContext, T> {
      return forListOrException(emptyList())
    }

    /**
     * Returns a multi provider that always returns null
     */
    @JvmStatic
    fun alwaysNull(): MultiProvider<Any, Nothing?> {
      return MultiProvider { null }
    }

    /**
     * Wraps the given lambda into a multi provider
     */
    operator fun <IndexContext, T> invoke(lambda: (index: Int) -> T): MultiProvider<IndexContext, T> {
      return MultiProvider { index -> lambda(index) }
    }
  }
}

/**
 * Returns a [MultiProvider] that delegates all calls to the current value of this property
 */
fun <IndexContext, T> KProperty0<MultiProvider<IndexContext, T>>.delegate(): MultiProvider<IndexContext, T> {
  return MultiProvider { index -> this@delegate.get().valueAt(index) }
}

/**
 * Maps the value.
 *
 * ATTENTION: Creates a new instance!
 *
 * ATTENTION: If using in styles, usually a delegate should be used first:
 *
 * `::baseProvider.delegate().map{...}`
 */
fun <IndexContext, T, R> MultiProvider<IndexContext, T>.mapping(function: (T) -> R): MultiProvider<IndexContext, R> {
  return MultiProvider.invoke {
    function(this.valueAt(it))
  }
}


/**
 * Marker annotation for annotations that can be used as IndexContent
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class MultiProviderIndexContextAnnotation
