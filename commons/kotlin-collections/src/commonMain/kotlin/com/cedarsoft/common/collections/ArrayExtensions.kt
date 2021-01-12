package com.cedarsoft.common.collections

import com.cedarsoft.common.kotlin.lang.wrapAround

/**
 *
 */


/**
 * Returns the n th element from this [Array]. Uses modulo if the index is larger than the size of the array
 */
fun <T> Array<T>.getModulo(index: Int): T {
  //This calculation produces a "wrap around" effect for negative indices
  return this[index.wrapAround(size)]
}

/**
 * An [ByteArray] of size 0
 */
private val emptyArrayOfBytes: ByteArray = ByteArray(0)

/**
 * Returns an [ByteArray] of size 0
 */
fun emptyByteArray(): ByteArray = emptyArrayOfBytes

/**
 * An [IntArray] of size 0
 */
private val emptyArrayOfInts: IntArray = IntArray(0)

/**
 * Returns an [IntArray] of size 0
 */
fun emptyIntArray(): IntArray = emptyArrayOfInts

/**
 * A [DoubleArray] of size 0
 */
private val emptyArrayOfDoubles: DoubleArray = DoubleArray(0)

/**
 * Returns a [DoubleArray] of size 0
 */
fun emptyDoubleArray(): DoubleArray = emptyArrayOfDoubles

/**
 * A [FloatArray] of size 0
 */
private val emptyArrayOfFloats: FloatArray = FloatArray(0)

/**
 * Returns a [FloatArray] of size 0
 */
fun emptyFloatArray(): FloatArray = emptyArrayOfFloats

