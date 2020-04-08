package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.math.BigInteger

/**
 * Serializer for BigInteger
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = BigInteger::class)
object BigIntegerSerializer : KSerializer<BigInteger> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor("BigInteger", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, obj: BigInteger) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): BigInteger {
    return BigInteger(decoder.decodeString())
  }
}
