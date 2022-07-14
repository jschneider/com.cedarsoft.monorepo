package it.neckar.uuid

import com.benasher44.uuid.Uuid

/**
 * Returns a new UUID by combining the bytes of both given UUIDs using XOR
 */
fun Uuid.xor(other: Uuid): Uuid {
  return Uuid(mostSignificantBits.xor(other.mostSignificantBits), leastSignificantBits.xor(other.leastSignificantBits))
}

/**
 * Creates a new UUID using XOR
 */
fun Uuid.xor(mostSignificantLong: Long, leastSignificantLong: Long): Uuid {
  return xor(Uuid(mostSignificantLong, leastSignificantLong))
}
