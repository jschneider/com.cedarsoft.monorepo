package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.time.Instant

/**
 * Serializer for Duration
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Instant::class)
object InstantSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor = StringDescriptor.withName("Instant")

  override fun serialize(encoder: Encoder, obj: Instant) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): Instant {
    return Instant.parse(decoder.decodeString())
  }
}