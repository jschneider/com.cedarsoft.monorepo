package com.cedarsoft.commons.provider

import com.cedarsoft.common.kotlin.lang.getModulo
import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty0

/**
 * Returns values for a given index.
 * Does *not* have a size. In most cases it is helpful to use a [SizedProvider] instead
 */
interface MultiProvider<out T> {
  /**
   * Retrieves the value at the given [index].
   */
  fun valueAt(index: Int): T

  /**
   * Retrieves the value at the given [index].
   * Use [valueAt] instead - for symmetry with other providers.
   */
  operator fun get(index: Int): T {
    return valueAt(index)
  }

  companion object {
    /**
     * Wraps the given list into a [ListModuloProvider].
     * Does *NOT* support empty lists
     */
    @JvmStatic
    fun <T> forListModulo(values: List<T>): ListModuloProvider<T> {
      return ListModuloProvider(values)
    }

    /**
     * Provides the content of the list.
     * Returns the provided fallback if the list is empty
     */
    @JvmStatic
    fun <T> forListModuloOrIfEmpty(values: List<T>, fallbackIfEmpty: T): ListModuloProviderWithFallback<T> {
      return ListModuloProviderWithFallback(values, fallbackIfEmpty)
    }

    @JvmStatic
    fun <T> modulo(vararg values: T): ListModuloProvider<T> {
      return forListModulo(values.toList())
    }

    /**
     * Wraps the given list into a [ListModuloProvider].
     * Returns the fallback value if the given [values] list is empty
     */
    @JvmStatic
    fun <T> forListModulo(values: List<T>, fallback: T): MultiProvider<T> {
      if (values.isEmpty()) {
        return always(fallback)
      }

      return ListModuloProvider(values)
    }

    /**
     * Returns the values from the list - and null for all indices that are not contained in the given list.
     * Supports empty lists
     */
    @JvmStatic
    fun <T> forListOrNull(values: List<T>): ListOrNullProvider<T> {
      return ListOrNullProvider(values)
    }

    /**
     * Returns the values of the list or throws an exception if there is no element in the list at the requested index.
     */
    @JvmStatic
    fun <T> forListOrException(values: List<T>): MultiProvider<T> {
      return ListOrExceptionProvider(values)
    }

    /**
     * Always returns the given value - for all indices
     */
    @JvmStatic
    fun <T> always(value: T): MultiProvider<T> {
      return object : MultiProvider<T> {
        override fun valueAt(index: Int): T {
          return value
        }
      }
    }

    @JvmStatic
    fun <T> empty(): MultiProvider<T> {
      @Suppress("UNCHECKED_CAST")
      return EmptyProvider as MultiProvider<T>
    }

    /**
     * Returns a multi provider that always returns null
     */
    @JvmStatic
    fun alwaysNull(): NullProvider {
      return NullProvider
    }

    /**
     * Wraps the given lambda into a multi provider
     */
    operator fun <T> invoke(lambda: (index: Int) -> T): MultiProvider<T> {
      return object : MultiProvider<T> {
        override fun valueAt(index: Int): T {
          return lambda(index)
        }
      }
    }
  }
}


/**
 * Returns the corresponding item using module.
 * Throws an exception if the list is empty
 */
open class ListModuloProvider<out T>(
  private val values: List<T>,
) : MultiProvider<T> {

  override fun valueAt(index: Int): T {
    require(values.isNotEmpty()) {
      "List must not be empty"
    }
    return values.getModulo(index)
  }
}

/**
 * Returns the corresponding item using module - returns the fallback if the list is empty
 */
open class ListModuloProviderWithFallback<out T>(
  private val values: List<T>,
  private val fallbackIfEmpty: T,
) : MultiProvider<T> {

  override fun valueAt(index: Int): T {
    if (values.isEmpty()) {
      return fallbackIfEmpty
    }

    return values.getModulo(index)
  }
}

/**
 * Returns the values of the list - or null for other indices.
 * Supports empty lists.
 */
open class ListOrNullProvider<out T>(
  private val values: List<T>,
) : MultiProvider<T?> {

  override fun valueAt(index: Int): T? {
    return values.getOrNull(index)
  }
}

/**
 * Returns the values of the list or throws an exception if there is no element in the list at the requested index.
 */
open class ListOrExceptionProvider<out T>(
  private val values: List<T>,
) : MultiProvider<T> {
  override fun valueAt(index: Int): T {
    return values[index];
  }
}

/**
 * Always returns null
 */
object NullProvider : MultiProvider<Nothing?> {
  override fun valueAt(index: Int): Nothing? {
    return null
  }
}

/**
 * Returns a [MultiProvider] that delegates all calls to the current value of this property
 */
fun <T> KProperty0<MultiProvider<T>>.delegate(): MultiProvider<T> {
  return DelegatingMultiProvider { get() }
}

/**
 * A [MultiProvider] that delegates the calls
 */
class DelegatingMultiProvider<T>(
  var delegateProvider: () -> MultiProvider<T>,
) : MultiProvider<T> {
  override fun valueAt(index: Int): T {
    return delegateProvider().valueAt(index)
  }
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
fun <T, R> MultiProvider<T>.mapping(function: (T) -> R): MultiProvider<R> {
  return MultiProvider.invoke {
    function(this[it])
  }
}
