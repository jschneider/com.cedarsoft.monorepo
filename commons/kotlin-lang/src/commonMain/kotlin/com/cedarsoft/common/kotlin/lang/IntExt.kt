package com.cedarsoft.common.kotlin.lang

import com.cedarsoft.unit.other.Exclusive

/**
 * A for loop that starts from 0 until this (exclusive)
 */
inline fun @Exclusive Int.fastFor(callback: (index: Int) -> Unit) {
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

/**
 * Returns true if the bit at the given index has been set
 */
inline fun Int.isBitSet(index: Int): Boolean {
  return this and (1 shl index) != 0
}
