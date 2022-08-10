package it.neckar.uuid

import kotlinx.serialization.Serializable

/**
 * Abstract base class for [HasUuid] that provides the equals/hashCode implementations as
 * required by [HasUuid].
 */
@Serializable
abstract class AbstractHasUuid : HasUuid {

  final override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as AbstractHasUuid

    if (uuid != other.uuid) return false

    return true
  }

  final override fun hashCode(): Int {
    return uuid.hashCode()
  }
}
