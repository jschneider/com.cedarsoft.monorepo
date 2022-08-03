package it.neckar.uuid

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Use like this:
 * `@file:UseSerializers(UuidSerializer::class)`
 */
object UuidSerializer : KSerializer<Uuid> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Uuid) {
    encoder.encodeString(value.toString())
  }

  override fun deserialize(decoder: Decoder): Uuid {
    val string = decoder.decodeString()
    return uuidFrom(string)
  }
}
