package com.cedarsoft.commons.kotlin.serializers

import com.google.common.collect.ImmutableList
import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.StructureKind

/**
 * Serializer for immutable lists
 */
@Serializer(forClass = ImmutableList::class)
class ImmutableListSerializer<T>(private val elementsSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {

  override val descriptor: SerialDescriptor = SerialDescriptor("ImmutableList", StructureKind.LIST)

  override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
    val size = value.size

    val compositeEncoder = encoder.beginCollection(descriptor, size, elementsSerializer)
    value.forEachIndexed { index, t ->
      compositeEncoder.encodeSerializableElement(descriptor, index, elementsSerializer, t)
    }

    compositeEncoder.endStructure(descriptor)
  }

  override fun deserialize(decoder: Decoder): ImmutableList<T> {
    val compositeDecoder = decoder.beginStructure(descriptor, elementsSerializer)

    val builder = ImmutableList.builder<T>()

    mainLoop@ while (true) {
      when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
        CompositeDecoder.READ_ALL  -> {
          throw UnsupportedOperationException("READ_ALL not supported")
        }
        CompositeDecoder.READ_DONE -> break@mainLoop
        else                       -> builder.add(compositeDecoder.decodeSerializableElement(descriptor, index, elementsSerializer))
      }
    }

    compositeDecoder.endStructure(descriptor)
    return builder.build()
  }
}
