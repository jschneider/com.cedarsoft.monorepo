package it.neckar.commons.tags

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TagsSerializer : KSerializer<Tags> {
  private val listSerializer = ListSerializer(String.serializer())

  override val descriptor: SerialDescriptor = listSerializer.descriptor

  override fun serialize(encoder: Encoder, value: Tags) {
    encoder.encodeSerializableValue(listSerializer, value.tags.map { it.id })
  }

  override fun deserialize(decoder: Decoder): Tags {
    val rawValues = decoder.decodeSerializableValue(listSerializer)
    return Tags(rawValues.map { Tag(it) }.toSet())
  }
}
