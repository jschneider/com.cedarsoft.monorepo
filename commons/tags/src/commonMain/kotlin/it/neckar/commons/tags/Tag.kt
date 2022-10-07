package it.neckar.commons.tags

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a single tag
 * Tags can not be translated.
 */
@JvmInline
@Serializable
value class Tag(val id: String) {

  operator fun plus(other: Tag): Tags {
    return Tags(setOf(this, other))
  }
}
