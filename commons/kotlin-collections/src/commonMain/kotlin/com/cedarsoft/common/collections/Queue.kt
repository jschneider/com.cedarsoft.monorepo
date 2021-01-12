package com.cedarsoft.common.collections

typealias Queue<TGen> = TGenQueue<TGen>

// AUTOGENERATED: DO NOT MODIFY MANUALLY!


/**
 * A FIFO (First In First Out) structure.
 */
class TGenQueue<TGen>() : Collection<TGen> {
  private val items = TGenDeque<TGen>()

  override val size: Int get() = items.size
  override fun isEmpty() = size == 0

  constructor(vararg items: TGen) : this() {
    for (item in items) enqueue(item)
  }

  fun enqueue(v: TGen) = run { items.addLast(v) }
  fun peek(): TGen? = items.firstOrNull()
  fun dequeue(): TGen = items.removeFirst()
  fun remove(v: TGen) = run { items.remove(v) }
  fun toList() = items.toList()
  fun clear() = items.clear()

  override fun contains(element: TGen): Boolean = items.contains(element)
  override fun containsAll(elements: Collection<TGen>): Boolean = items.containsAll(elements)
  override fun iterator(): Iterator<TGen> = items.iterator()

  override fun hashCode(): Int = items.hashCode()
  override fun equals(other: Any?): Boolean = (other is TGenQueue<*/*TGen*/>) && items == other.items
}


// Int

/**
 * A FIFO (First In First Out) structure.
 */
class IntQueue() : Collection<Int> {
  private val items = IntDeque()

  override val size: Int get() = items.size
  override fun isEmpty() = size == 0

  constructor(vararg items: Int) : this() {
    for (item in items) enqueue(item)
  }

  fun enqueue(v: Int) = run { items.addLast(v) }
  fun peek(): Int? = items.firstOrNull()
  fun dequeue(): Int = items.removeFirst()
  fun remove(v: Int) = run { items.remove(v) }
  fun toList() = items.toList()
  fun clear() = items.clear()

  override fun contains(element: Int): Boolean = items.contains(element)
  override fun containsAll(elements: Collection<Int>): Boolean = items.containsAll(elements)
  override fun iterator(): Iterator<Int> = items.iterator()

  override fun hashCode(): Int = items.hashCode()
  override fun equals(other: Any?): Boolean = (other is IntQueue) && items == other.items
}


// Double

/**
 * A FIFO (First In First Out) structure.
 */
class DoubleQueue() : Collection<Double> {
  private val items = DoubleDeque()

  override val size: Int get() = items.size
  override fun isEmpty() = size == 0

  constructor(vararg items: Double) : this() {
    for (item in items) enqueue(item)
  }

  fun enqueue(v: Double) = run { items.addLast(v) }
  fun peek(): Double? = items.firstOrNull()
  fun dequeue(): Double = items.removeFirst()
  fun remove(v: Double) = run { items.remove(v) }
  fun toList() = items.toList()
  fun clear() = items.clear()

  override fun contains(element: Double): Boolean = items.contains(element)
  override fun containsAll(elements: Collection<Double>): Boolean = items.containsAll(elements)
  override fun iterator(): Iterator<Double> = items.iterator()

  override fun hashCode(): Int = items.hashCode()
  override fun equals(other: Any?): Boolean = (other is DoubleQueue) && items == other.items
}


// Float

/**
 * A FIFO (First In First Out) structure.
 */
class FloatQueue() : Collection<Float> {
  private val items = FloatDeque()

  override val size: Int get() = items.size
  override fun isEmpty() = size == 0

  constructor(vararg items: Float) : this() {
    for (item in items) enqueue(item)
  }

  fun enqueue(v: Float) = run { items.addLast(v) }
  fun peek(): Float? = items.firstOrNull()
  fun dequeue(): Float = items.removeFirst()
  fun remove(v: Float) = run { items.remove(v) }
  fun toList() = items.toList()
  fun clear() = items.clear()

  override fun contains(element: Float): Boolean = items.contains(element)
  override fun containsAll(elements: Collection<Float>): Boolean = items.containsAll(elements)
  override fun iterator(): Iterator<Float> = items.iterator()

  override fun hashCode(): Int = items.hashCode()
  override fun equals(other: Any?): Boolean = (other is FloatQueue) && items == other.items
}
