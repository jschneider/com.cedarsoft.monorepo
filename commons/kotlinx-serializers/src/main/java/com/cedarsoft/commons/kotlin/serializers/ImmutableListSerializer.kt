package com.cedarsoft.commons.kotlin.serializers

import com.google.common.collect.ImmutableList
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for immutable lists
 */
@Serializer(forClass = ImmutableList::class)
class ImmutableListSerializer<T>(private val elementsSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {

  @OptIn(InternalSerializationApi::class)
  override val descriptor: SerialDescriptor = buildSerialDescriptor("ImmutableList", StructureKind.LIST)

  override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
    val size = value.size

    val compositeEncoder = encoder.beginCollection(descriptor, size)
    value.forEachIndexed { index, t ->
      compositeEncoder.encodeSerializableElement(descriptor, index, elementsSerializer, t)
    }

    compositeEncoder.endStructure(descriptor)
  }

  override fun deserialize(decoder: Decoder): ImmutableList<T> {
    val compositeDecoder = decoder.beginStructure(descriptor)

    val builder = ImmutableList.builder<T>()

    mainLoop@ while (true) {
      when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
        CompositeDecoder.DECODE_DONE -> break@mainLoop
        else                         -> builder.add(compositeDecoder.decodeSerializableElement(descriptor, index, elementsSerializer))
      }
    }

    compositeDecoder.endStructure(descriptor)
    return builder.build()
  }
}
