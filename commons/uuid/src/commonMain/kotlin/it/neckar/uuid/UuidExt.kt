package it.neckar.uuid

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.cedarsoft.common.kotlin.lang.ExecutionEnvironment
import com.cedarsoft.common.kotlin.lang.random

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

/**
 * Creates a new random UUID.
 * The random generator can be configured for tests
 */
fun randomUuid4(): Uuid {
  if (ExecutionEnvironment.inUnitTest) {
    return pseudoRandomUuid4()
  }

  return uuid4()
}

/**
 * Returns a new UUID using the pseudo random generator.
 *
 * ATTENTION: Do *not* use for production. Use [randomUuid4] instead!
 */
fun pseudoRandomUuid4(): Uuid {
  return Uuid(
    random.nextLong(),
    random.nextLong(),
  )
}
