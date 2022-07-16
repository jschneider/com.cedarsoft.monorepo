package com.cedarsoft.commons.provider

/**
 * A provider that takes one parameter and provides multiple doubles
 */
interface DoublesProvider1<in P1> : HasSize1<P1> {

  /**
   * Retrieves the value at the given [index].
   * @param index a value between 0 (inclusive) and [size] (exclusive)
   */
  fun valueAt(index: Int, param1: P1): Double

  /**
   * Computes the sum of all values.
   *
   * Returns 0.0 if there are no values.
   */
  fun sum(param1: P1): Double {
    var sum = 0.0
    for (index in 0 until size(param1)) {
      sum += valueAt(index, param1)
    }
    return sum
  }

  companion object {
    /**
     * An empty values provider that does not return any values
     */
    fun <P1> empty(): DoublesProvider1<P1> {
      return object : DoublesProvider1<P1> {
        override fun size(param1: P1): Int = 0

        override fun valueAt(index: Int, param1: P1): Double {
          throw UnsupportedOperationException("Must not be called")
        }
      }
    }
  }
}

/**
 * Converts a [DoublesProvider] to a [DoublesProvider1]
 */
fun <P1> DoublesProvider.asDoublesProvider1(): DoublesProvider1<P1> {
  val delegate = this

  return object : DoublesProvider1<P1> {
    override fun valueAt(index: Int, param1: P1): Double {
      return delegate.valueAt(index)
    }

    override fun size(param1: P1): Int {
      return delegate.size()
    }
  }
}
