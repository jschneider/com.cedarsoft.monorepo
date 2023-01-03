package com.cedarsoft.commons.provider

import com.cedarsoft.common.collections.Cache
import com.cedarsoft.common.collections.cache
import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty0

/**
 * Returns multiple values - also has a size.
 * The index provided to [valueAt] is *always* a value from 0..[size].
 * It is never an index from another context!
 *
 * ATTENTION: For primitive types, use the primitive variants to avoid boxing:
 * * [DoublesProvider]
 * * [IndexProvider]
 */
interface SizedProvider<out T> : MultiProvider<SizedProviderIndex, T>, HasSize {

  /**
   * Returns the first element
   * Throws a [NoSuchElementException] if there are no elements
   */
  fun first(): T {
    if (size() == 0) {
      throw NoSuchElementException("Size is 0")
    }
    return this.valueAt(0)
  }

  /**
   * Returns the last element.
   * Throws a [NoSuchElementException] if there are no elements
   */
  fun last(): T {
    val currentSize = size()
    if (currentSize == 0) {
      throw NoSuchElementException("Size is 0")
    }
    return this.valueAt(currentSize - 1)
  }

  /**
   * Returns the element at the given index or null if the index is >= [size]
   */
  fun getOrNull(index: @SizedProviderIndex Int): T? {
    if (index >= size()) {
      return null
    }

    return valueAt(index)
  }

  override fun valueAt(index: @SizedProviderIndex Int): T

  companion object {
    /**
     * Wraps the given list into a [ListSizedProvider]
     */
    @JvmStatic
    fun <T> forList(values: List<T>): ListSizedProvider<T> {
      return ListSizedProvider(values)
    }

    /**
     * Maps the elements of the list
     */
    fun <T, R> mapped(values: List<T>, function: (T) -> R): SizedProvider<R> {
      return object : SizedProvider<R> {
        override fun size(): Int {
          return values.size
        }

        override fun valueAt(index: Int): R {
          return function(values[index])
        }
      }
    }

    /**
     * Wraps the given elements into a [ListSizedProvider]
     */
    @JvmStatic
    fun <T> forValues(vararg elements: T): ListSizedProvider<T> {
      return forList(elements.toList())
    }

    @JvmStatic
    fun <T> empty(): SizedProvider<T> {
      @Suppress("UNCHECKED_CAST")
      return EmptyProvider as SizedProvider<T>
    }

    /**
     * Returns a sized provider that repeats the given value n times
     */
    @JvmStatic
    fun <T> repeat(value: T, count: Int): SizedProvider<T> {
      return RepeatingProvider(value, count)
    }

    /**
     * Returns a sized provider that uses the given provider to return the objects
     */
    fun <T> of(size: Int, provider: (index: Int) -> T): SizedProvider<T> {
      return object : SizedProvider<T> {
        override fun valueAt(index: Int): T {
          return provider(index)
        }

        override fun size(): Int = size
      }
    }
  }
}

/**
 * Returns the values from the given list
 *
 * Attention: The list passed to this class must be immutable.
 */
open class ListSizedProvider<out T>(
  private val values: List<T>,
) : SizedProvider<T> {
  override fun size(): Int = values.size

  override fun valueAt(index: Int): T {
    return values[index]
  }
}


/**
 * Skips values if the given predicate returns false. Returns null for skipped values
 */
class FilteredSizedProvider<out T>(
  val delegate: SizedProvider<T>,
  /**
   * The filter that is used to determine whether a value shall be returned.
   * If true is returned, the value is used.
   * If false is returned, the value is *not* used (it is skipped)
   */
  val predicate: (index: Int) -> Boolean,

  ) : SizedProvider<T?> {
  override fun size(): Int = delegate.size()

  override fun valueAt(index: Int): T? {
    if (predicate(index)) {
      return delegate.valueAt(index)
    }

    return null
  }
}

/**
 * Repeats this value
 */
class RepeatingProvider<T>(val value: T, val count: Int) : SizedProvider<T> {
  override fun size(): Int = count

  override fun valueAt(index: Int): T {
    return value
  }
}

/**
 * Does not contain any values - throws an exception if a values is requested
 */
object EmptyProvider : SizedProvider<Any> {
  override fun size(): Int = 0

  override fun isEmpty(): Boolean {
    return true
  }

  override fun valueAt(index: Int): Any {
    throw UnsupportedOperationException("No values available")
  }
}


/**
 * Only returns the values if the given condition returns true for the index
 */
fun <T> SizedProvider<T>.onlyIf(predicate: (index: Int) -> Boolean): FilteredSizedProvider<T> {
  return FilteredSizedProvider(this, predicate)
}

/**
 * Caches the results of a multi provider
 */
class CachedSizedProvider<T>(
  /**
   * The delegate that is used to create the multi
   */
  val delegate: SizedProvider<T>,
  /**
   * The maximum size of the cache
   */
  cacheSize: Int = 500,
) : SizedProvider<T> {

  /**
   * The cache
   */
  val cache: Cache<Int, T> = cache("CachedSizedProvider", cacheSize)

  override fun size(): Int = delegate.size()

  override fun valueAt(index: Int): T {
    return cache.getOrStore(index) {
      delegate.valueAt(index)
    }
  }

  /**
   * Clears the cache
   */
  fun clear() {
    cache.clear()
  }
}

/**
 * Caches the multi provider
 */
fun <T> SizedProvider<T>.cached(cacheSize: Int = 100): CachedSizedProvider<T> {
  return CachedSizedProvider(this, cacheSize)
}

/**
 * Returns a delegate that uses the current value of this property to delegate all calls.
 */
fun <T> KProperty0<SizedProvider<T>>.delegate(): SizedProvider<T> {
  return DelegatingSizedProvider { get() }
}

/**
 * A sized provider that delegates the calls
 */
class DelegatingSizedProvider<T>(
  val delegateProvider: () -> SizedProvider<T>,
) : SizedProvider<T> {
  override fun size(): Int = delegateProvider().size()

  override fun valueAt(index: Int): T {
    return delegateProvider().valueAt(index)
  }
}

/**
 * Marker annotation for index related to the size of the sized provider
 */
@Target(AnnotationTarget.TYPE)
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@MultiProviderIndexContextAnnotation
annotation class SizedProviderIndex


/**
 * Casts a sized provider to a multi provider
 */
inline fun <IndexContext, T> SizedProvider<T>.asMultiProvider(): MultiProvider<IndexContext, T> {
  return this as MultiProvider<IndexContext, T>
}

/**
 * Maps the value.
 *
 * ATTENTION: Creates a new instance!
 */
fun <T, R> SizedProvider<T>.mapped(function: (T) -> R): SizedProvider<R> {
  return object : SizedProvider<R> {
    override fun size(): Int {
      return this@mapped.size()
    }

    override fun valueAt(index: Int): R {
      return function(this@mapped.valueAt(index))
    }
  }
}
