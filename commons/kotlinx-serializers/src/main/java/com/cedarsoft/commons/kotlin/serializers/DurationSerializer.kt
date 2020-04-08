package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.time.Duration

/**
 * Serializer for Duration
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Duration::class)
object DurationSerializer : KSerializer<Duration> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor("Duration", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, obj: Duration) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): Duration {
    return Duration.parse(decoder.decodeString())
  }
}
