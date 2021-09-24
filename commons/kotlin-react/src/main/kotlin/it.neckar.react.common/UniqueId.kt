package it.neckar.react.common

import react.*

/**
 * Provides unique IDs
 */
object UniqueId {
  private var counter: Int = 0

  /**
   * Returns a unique ID with the given prefix
   */
  fun create(prefix: String): String {
    counter++
    return "$prefix$counter"
  }
}

/**
 * Creates a unique ID using [useMemo] hook.
 */
fun uniqueIdMemo(prefix: String): String {
  return useMemo {
    UniqueId.create(prefix)
  }
}
