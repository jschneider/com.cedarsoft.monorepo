package com.cedarsoft.commons.provider


inline fun DoublesProvider.fastForEach(callback: (Double) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(this.valueAt(n++))
  }
}

inline fun DoublesProvider.fastForEachIndexed(callback: (index: Int, value: Double) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(n, this.valueAt(n))
    n++
  }
}

inline fun <T> DoublesProvider1<T>.fastForEach(param1: T, callback: (Double) -> Unit) {
  var n = 0
  val currentSize = size(param1)
  while (n < currentSize) {
    callback(this.valueAt(n++, param1))
  }
}

inline fun <T> DoublesProvider1<T>.fastForEachIndexed(param1: T, callback: (index: Int, value: Double) -> Unit) {
  val currentSize = size(param1)
  fastForEachIndexed(currentSize, param1, callback)
}

inline fun <T> DoublesProvider1<T>.fastForEachIndexed(actualSize: Int, param1: T, callback: (index: Int, value: Double) -> Unit) {
  var n = 0
  while (n < actualSize) {
    callback(n, this.valueAt(n, param1))
    n++
  }
}

inline fun <T> SizedProvider<T>.fastForEach(callback: (T) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(this.valueAt(n++))
  }
}

inline fun <T> SizedProvider<T>.fastForEachIndexed(callback: (index: Int, value: T) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(n, this.valueAt(n))
    n++
  }
}

inline fun <T, P1> SizedProvider1<T, P1>.fastForEach(param1: P1, callback: (T) -> Unit) {
  var n = 0
  val currentSize = size(param1)
  while (n < currentSize) {
    callback(this.valueAt(n++, param1))
  }
}

inline fun <T, P1> SizedProvider1<T, P1>.fastForEachIndexed(param1: P1, callback: (index: Int, value: T) -> Unit) {
  var n = 0
  val currentSize = size(param1)
  while (n < currentSize) {
    callback(n, this.valueAt(n, param1))
    n++
  }
}

inline fun <T : Index> IndexProvider<T>.fastForEach(callback: (index: T) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(this.valueAt(n++))
  }
}

/**
 * Iterates over the provided indices.
 * This method is only useful in very few cases!
 *
 * ATTENTION: "i" is very different to the provided index
 */
inline fun <T : Index> IndexProvider<T>.fastForEachIndexed(callback: (i: Int, value: T) -> Unit) {
  var n = 0
  val currentSize = size()
  while (n < currentSize) {
    callback(n, this.valueAt(n))
    n++
  }
}
