package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration

/**
 * Serializer for Duration
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Duration::class)
object DurationSerializer : KSerializer<Duration> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, obj: Duration) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): Duration {
    return Duration.parse(decoder.decodeString())
  }
}
