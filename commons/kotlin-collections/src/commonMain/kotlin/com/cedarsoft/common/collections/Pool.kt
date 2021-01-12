package com.cedarsoft.common.collections

/**
 * Structure containing a set of reusable objects.
 *
 * The method [alloc] retrieves from the pool or allocates a new object,
 * while the [free] method pushes back one element to the pool and resets it to reuse it.
 */
class Pool<T>(private val reset: (T) -> Unit = {}, preallocate: Int = 0, private val gen: (Int) -> T) {
  companion object {
    fun <T : Poolable> fromPoolable(preallocate: Int = 0, gen: (Int) -> T): Pool<T> =
      Pool(reset = { it.reset() }, preallocate = preallocate, gen = gen)
  }

  constructor(preallocate: Int = 0, gen: (Int) -> T) : this({}, preallocate, gen)

  private val items = Stack<T>()
  private var lastId = 0

  val itemsInPool: Int get() = items.size

  init {
    for (n in 0 until preallocate) items.push(gen(lastId++))
  }

  fun alloc(): T = if (items.isNotEmpty()) items.pop() else gen(lastId++)

  interface Poolable {
    fun reset()
  }

  fun free(element: T) {
    reset(element)
    items.push(element)
  }

  fun free(vararg elements: T) = run { for (element in elements) free(element) }

  fun free(elements: Iterable<T>) = run { for (element in elements) free(element) }

  inline fun <R> alloc(callback: (T) -> R): R {
    val temp = alloc()
    try {
      return callback(temp)
    } finally {
      free(temp)
    }
  }

  inline fun <R> allocThis(callback: T.() -> R): R {
    val temp = alloc()
    try {
      return callback(temp)
    } finally {
      free(temp)
    }
  }


  override fun hashCode(): Int = items.hashCode()
  override fun equals(other: Any?): Boolean = (other is Pool<*>) && this.items == other.items && this.itemsInPool == other.itemsInPool
}
