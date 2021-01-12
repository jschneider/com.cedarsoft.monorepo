package com.cedarsoft.common.collections

/**
 * Contains extensions for KDS classes
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

fun IntArray2.Companion.invokeRows(rows: Array<IntArray>): IntArray2 {
  val width = rows[0].size
  val height = rows.size
  val anyCell = rows[0][0]
  return (IntArray2(width, height) { anyCell }).apply { setRows(rows) }
}

fun IntArray2.setRows(rows: Array<IntArray>) {
  var n = 0
  for (y in rows.indices) {
    val row = rows[y]
    for (x in row.indices) {
      this.data[n++] = row[x]
    }
  }
}

fun IntArray2.Companion.invokeCols(cols: Array<IntArray>): IntArray2 {
  val width = cols.size
  val height = cols[0].size
  val anyCell = cols[0][0]
  return (IntArray2(width, height) { anyCell }).apply { setCols(cols) }
}

fun IntArray2.setCols(cols: Array<IntArray>) {
  var n = 0
  for (x in cols.indices) {
    val column = cols[x]
    for (y in column.indices) {
      val value = column[y]
      this[x, y] = value
    }
  }
}

/**
 * Returns a new array with the new width
 */
fun IntArray2.withHeight(newHeight: Int): IntArray2 {
  val newData = IntArray(width * newHeight)
  data.copyInto(newData, 0, 0, data.size)
  return IntArray2(width, newHeight, newData)
}

/**
 * Returns an empty IntArray2
 */
fun IntArray2.Companion.empty(): IntArray2 {
  return IntArray2(0, 0, 0)
}

/**
 * Emulates a constructor
 */
@Suppress("FunctionName")
fun IntArrayList(initialSize: Int = 7, fill: (Int) -> Int): IntArrayList {
  return IntArrayList(initialSize).also {
    for (i in 0 until initialSize) {
      it.add(fill(i))
    }
  }
}

/**
 * Emulates a constructor
 */
@Suppress("FunctionName")
fun DoubleArrayList(initialSize: Int = 7, fill: (Int) -> Double): DoubleArrayList {
  return DoubleArrayList(initialSize).also {
    for (i in 0 until initialSize) {
      it.add(fill(i))
    }
  }
}


/**
 * Emulates a constructor
 */
@Suppress("FunctionName")
fun FloatArrayList(initialSize: Int = 7, fill: (Int) -> Float): FloatArrayList {
  return FloatArrayList(initialSize).also {
    for (i in 0 until initialSize) {
      it.add(fill(i))
    }
  }
}

/**
 *
 */
fun IntArrayList.isNotEmpty(): Boolean = !isEmpty()
fun IntArrayList.lastOrNull(): Int? = if (isEmpty()) null else this.getAt(size - 1)

fun DoubleArrayList.isNotEmpty(): Boolean = !isEmpty()
fun DoubleArrayList.lastOrNull(): Double? = if (isEmpty()) null else this.getAt(size - 1)

fun FloatArrayList.isNotEmpty(): Boolean = !isEmpty()
fun FloatArrayList.lastOrNull(): Float? = if (isEmpty()) null else this.getAt(size - 1)


fun DoubleArray.minOrElse(nil: Double): Double {
  if (isEmpty()) return nil
  var out = Double.POSITIVE_INFINITY
  for (i in 0..lastIndex) out = kotlin.math.min(out, this[i])
  return out
}

fun DoubleArray.maxOrElse(nil: Double): Double {
  if (isEmpty()) return nil
  var out = Double.NEGATIVE_INFINITY
  for (i in 0..lastIndex) out = kotlin.math.max(out, this[i])
  return out
}
