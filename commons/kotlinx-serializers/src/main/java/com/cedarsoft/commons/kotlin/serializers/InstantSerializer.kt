package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.time.Instant

/**
 * Serializer for Duration
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Instant::class)
object InstantSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor("Instant", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, obj: Instant) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): Instant {
    return Instant.parse(decoder.decodeString())
  }
}
