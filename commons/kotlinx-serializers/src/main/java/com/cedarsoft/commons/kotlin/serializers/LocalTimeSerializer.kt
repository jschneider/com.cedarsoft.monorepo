package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Serializer for LocalTime
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = LocalTime::class)
object LocalTimeSerializer : KSerializer<LocalTime> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor("LocalTime", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, obj: LocalTime) {
    encoder.encodeString(obj.format(DateTimeFormatter.ISO_TIME))
  }

  override fun deserialize(decoder: Decoder): LocalTime {
    return LocalTime.from(DateTimeFormatter.ISO_TIME.parse(decoder.decodeString()))
  }
}
