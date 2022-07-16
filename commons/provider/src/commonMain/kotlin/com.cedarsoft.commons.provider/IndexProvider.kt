package com.cedarsoft.commons.provider

import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty0

/**
 * Provides an index for an index.
 */
interface IndexProvider<out T : Index> : HasSize {
  /**
   * Retrieves the index at the given [index].
   *
   * @param index a value between 0 (inclusive) and [size] (exclusive)
   */
  fun valueAt(index: Int): T

  companion object {
    /**
     * An empty provider that does not return any values
     */
    fun <T : Index> empty(): IndexProvider<T> {
      return empty as IndexProvider<T>
    }

    private val empty: IndexProvider<*> = object : IndexProvider<Index> {
      override fun size(): Int = 0

      override fun valueAt(index: Int): Index {
        throw UnsupportedOperationException("Must not be called")
      }
    }

    /**
     * Creates a new [IndexProvider] that returns the given values
     */
    @JvmStatic
    fun <T : Index> forValues(vararg values: T): IndexProvider<T> {
      return object : IndexProvider<T> {
        override fun valueAt(index: Int): T {
          return values[index]
        }

        override fun size(): Int {
          return values.size
        }
      }
    }

    /**
     * ATTENTION! Use forValues instead (if possible).
     * This method should only be used in very rare cases because of boxing!
     */
    @JvmStatic
    fun <T : Index> forList(values: List<T>): IndexProvider<T> {
      return object : IndexProvider<T> {
        override fun valueAt(index: Int): T {
          return values[index]
        }

        override fun size(): Int {
          return values.size
        }
      }
    }
  }
}

/**
 * Represents an index that is provided by an index provider.
 * Subclasses should be value classes to avoid boxing!
 */
interface Index {
  val value: Int
}

/**
 * Returns a delegate that uses the current value of this property to delegate all calls.
 */
fun <T : Index> KProperty0<IndexProvider<T>>.delegate(): IndexProvider<T> {
  return DelegatingIndexProvider { get() }
}

/**
 * A sized provider that delegates the calls
 */
class DelegatingIndexProvider<T : Index>(
  val delegateProvider: () -> IndexProvider<T>,
) : IndexProvider<T> {
  override fun size(): Int = delegateProvider().size()

  override fun valueAt(index: Int): T {
    return delegateProvider().valueAt(index)
  }
}
