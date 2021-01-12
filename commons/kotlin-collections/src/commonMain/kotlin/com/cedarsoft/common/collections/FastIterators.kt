package com.cedarsoft.common.collections

/**
 *
 */

/**
 * Iterates over the entries of this [ByteArray]
 */
inline fun ByteArray.fastForEach(callback: (value: Byte) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this[n++])
  }
}

/**
 * Iterates over the entries of this [ByteArray]
 */
inline fun ByteArray.fastForEachIndexed(callback: (index: Int, value: Byte) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this[n++])
  }
}

/**
 * Iterates over the entries of a int array
 *
 * Do *not* modify the underlying data structure while iterating
 */
inline fun IntArray.fastForEach(callback: (value: Int) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this[n++])
  }
}

inline fun IntArray.fastForEachIndexed(callback: (index: Int, value: Int) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this[n++])
  }
}

/**
 * Iterates over the entries of a double array
 *
 * Do *not* modify the underlying data structure while iterating
 */
inline fun DoubleArray.fastForEach(callback: (value: Double) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this[n++])
  }
}

/**
 * Iterates over the entries of a double array
 *
 * Do *not* modify the underlying data structure while iterating
 */
inline fun DoubleArray.fastForEachIndexed(callback: (index: Int, value: Double) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this[n++])
  }
}

/**
 * Iterates over the entries of a list
 *
 * Do *not* modify the underlying data structure while iterating
 */
inline fun <T> List<T>.fastForEach(callback: (value: T) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this[n++])
  }
}

inline fun <T, V> List<T>.fastMapNotNull(mapper: (value: T) -> V?): List<V> {
  val targetList = mutableListOf<V>()

  this.fastForEach { original ->
    mapper(original)?.let {
      targetList.add(it)
    }
  }

  return targetList
}

inline fun <T, V> List<T>.fastMap(mapper: (value: T) -> V): List<V> {
  val targetList = mutableListOf<V>()

  this.fastForEach { original ->
    mapper(original).let {
      targetList.add(it)
    }
  }

  return targetList
}

inline fun <V> DoubleArray.fastMap(mapper: (value: Double) -> V): List<V> {
  val targetList = mutableListOf<V>()

  this.fastForEach { original ->
    mapper(original).let {
      targetList.add(it)
    }
  }

  return targetList
}

inline fun DoubleArray.fastMapDouble(mapper: (value: Double) -> Double): DoubleArray {
  return DoubleArray(size) {
    mapper(this[it])
  }
}

inline fun <S, T : S> List<T>.fastReduce(operation: (acc: S, T) -> S): S {
  require(this.isNotEmpty()) { "not supported for empty lists" }

  var reduced: S = get(0)

  fastForEachIndexed { index, value ->
    if (index == 0) {
      return@fastForEachIndexed
    }

    reduced = operation(reduced, value)
  }

  return reduced
}

inline fun <T> List<T>.fastForEachReversed(callback: (T) -> Unit) {
  val currentSize = size
  var n = currentSize - 1
  while (n >= 0) {
    callback(this[n])
    n--
  }
}

inline fun <T> List<T>.fastForEachFiltered(predicate: (T) -> Boolean, callback: (value: T) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    val element = this[n++]

    if (predicate(element)) {
      callback(element)
    }
  }
}

inline fun DoubleArray.fastForEachFiltered(predicate: (Double) -> Boolean, callback: (value: Double) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    val element = this[n++]

    if (predicate(element)) {
      callback(element)
    }
  }
}

inline fun <T> Array<T>.fastForEach(callback: (T) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this[n++])
  }
}

inline fun <T> Array<T>.fastForEachReversed(callback: (T) -> Unit) {
  val currentSize = size
  var n = currentSize - 1
  while (n >= 0) {
    callback(this[n])
    n--
  }
}

inline fun IntArrayList.fastForEach(callback: (Int) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this.getAt(n++))
  }
}

inline fun FloatArrayList.fastForEach(callback: (Float) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this.getAt(n++))
  }
}

inline fun DoubleArrayList.fastForEach(callback: (Double) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(this.getAt(n++))
  }
}

/**
 * Returns true if at least one of the elements matches the given check.
 * [check] is called for all elements until it returns true.
 */
inline fun DoubleArrayList.fastFindAny(check: (Double) -> Boolean): Boolean {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    if (check(this.getAt(n++))) {
      return true
    }
  }

  return false
}


/**
 * Iterates over the entries of a list
 *
 * Do *not* modify the underlying data structure while iterating
 */
inline fun <T> List<T>.fastForEachIndexed(callback: (index: Int, value: T) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this[n])
    n++
  }
}

inline fun <T> Array<T>.fastForEachIndexed(callback: (index: Int, value: T) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this[n])
    n++
  }
}

inline fun IntArrayList.fastForEachIndexed(callback: (index: Int, value: Int) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this.getAt(n))
    n++
  }
}

inline fun FloatArrayList.fastForEachIndexed(callback: (index: Int, value: Float) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this.getAt(n))
    n++
  }
}

inline fun DoubleArrayList.fastForEachIndexed(callback: (index: Int, value: Double) -> Unit) {
  var n = 0
  val currentSize = size
  while (n < currentSize) {
    callback(n, this.getAt(n))
    n++
  }
}

/**
 * A for loop that starts from 0 until this (exclusive)
 */
inline fun Int.fastFor(callback: (index: Int) -> Unit) {
  for (i in 0 until this) {
    callback(i)
  }
}

/**
 * Calls a callback for each element and one for each space between two numbers.
 * The [separator] is therefore called once less than the [callback].
 */
inline fun Int.join(separator: (indexBefore: Int) -> Unit, callback: (index: Int) -> Unit) {
  if (this == 0) {
    return
  }

  callback(0)
  for (i in 1 until this) {
    separator(i - 1)
    callback(i)
  }
}

/**
 * Maps every integer value from 0 until this (exclusive) and returns a list of the mapped values.
 */
inline fun <V> Int.fastMap(mapper: (value: Int) -> V): List<V> {
  val targetList = mutableListOf<V>()
  this.fastFor {
    targetList.add(mapper(it))
  }
  return targetList
}
