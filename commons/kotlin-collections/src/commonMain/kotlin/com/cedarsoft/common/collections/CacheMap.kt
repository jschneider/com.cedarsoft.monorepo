package com.cedarsoft.common.collections

open class CacheMap<K, V> private constructor(
  private val map: LinkedHashMap<K, V> = LinkedHashMap(),
  val maxSize: Int = 16,
  val free: (K, V) -> Unit = { _, _ -> }
) : MutableMap<K, V> by map {
  constructor(
    maxSize: Int = 16,
    free: (K, V) -> Unit = { _, _ -> }
  ) : this(LinkedHashMap(), maxSize, free)

  override val size: Int get() = map.size

  /**
   * Marks the entry with the given [key] as new
   */
  fun markAsNew(key: K) {
    // do not call 'free' here
    val value = map.remove(key)
    value?.let {
      map[key] = value
    }
  }

  override fun remove(key: K): V? {
    val value = map.remove(key)
    if (value != null) free(key, value)
    return value
  }

  override fun putAll(from: Map<out K, V>) = run { for ((k, v) in from) put(k, v) }
  override fun put(key: K, value: V): V? {
    if (size >= maxSize && !map.containsKey(key)) remove(map.keys.first())

    val oldValue = map[key]
    if (oldValue != value) {
      remove(key) // remove entry first to force a refresh when the new value is put into the map
      map[key] = value
    }
    return oldValue
  }

  override fun clear() {
    val keys = map.keys.toList()
    for (key in keys) remove(key)
  }

  override fun toString(): String = map.toString()

  override fun equals(other: Any?): Boolean = (other is CacheMap<*, *>) && (this.map == other.map) && (this.free == other.free)
  override fun hashCode(): Int = this.map.hashCode() + maxSize
}
