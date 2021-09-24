package com.cedarsoft.i18n

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * A unique key to identify a certain piece of text
 */
@kotlinx.serialization.Serializable
class TextKey(
  /**
   * The key
   */
  val key: String,
  /**
   * The fallback text that may be used if no resolved text is available
   */
  val fallbackText: String
) {

  override fun toString(): String {
    return "$key [$fallbackText]"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is TextKey) return false

    if (key != other.key) return false

    return true
  }

  override fun hashCode(): Int {
    return key.hashCode()
  }

  companion object {
    /**
     * Creates a new text key with the given default text that is also used as key
     */
    @JvmStatic
    fun simple(fallbackText: String): TextKey {
      return TextKey(fallbackText, fallbackText)
    }

    /**
     * A [TextKey] for the empty string
     */
    @JvmField
    val empty: TextKey = simple("")
  }
}
