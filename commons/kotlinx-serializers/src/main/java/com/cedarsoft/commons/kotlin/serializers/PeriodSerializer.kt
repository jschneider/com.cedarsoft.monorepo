package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.time.Period

/**
 * Serializer for Period
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Period::class)
object PeriodSerializer : KSerializer<Period> {
  override val descriptor: SerialDescriptor = StringDescriptor.withName("Period")

  override fun serialize(encoder: Encoder, obj: Period) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): Period {
    return Period.parse(decoder.decodeString())
  }
}