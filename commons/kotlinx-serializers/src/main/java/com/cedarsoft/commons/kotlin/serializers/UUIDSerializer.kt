package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.util.UUID

/**
 * Serializer for UUID
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
  override val descriptor: SerialDescriptor = StringDescriptor.withName("UUID")

  override fun serialize(encoder: Encoder, obj: UUID) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): UUID {
    return UUID.fromString(decoder.decodeString())
  }
}