package com.cedarsoft.commons.kotlin.serializers

import com.google.common.collect.ImmutableSet
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
@Serializer(forClass = ImmutableSet::class)
class ImmutableSetSerializer<T>(private val elementsSerializer: KSerializer<T>) : KSerializer<ImmutableSet<T>> {

  override val descriptor: SerialDescriptor = SerialDescriptor("ImmutableSet", StructureKind.LIST)

  override fun serialize(encoder: Encoder, obj: ImmutableSet<T>) {

    val size = obj.size

    val compositeEncoder = encoder.beginCollection(descriptor, size, elementsSerializer)
    obj.forEachIndexed { index, t ->
      compositeEncoder.encodeSerializableElement(descriptor, index, elementsSerializer, t)
    }

    compositeEncoder.endStructure(descriptor)
  }

  override fun deserialize(decoder: Decoder): ImmutableSet<T> {
    val compositeDecoder = decoder.beginStructure(descriptor, elementsSerializer)

    val builder = ImmutableSet.builder<T>()

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
