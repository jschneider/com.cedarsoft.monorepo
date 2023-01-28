package com.cedarsoft.commons.kotlin.serializers

import com.cedarsoft.common.collections.IntArray2
import com.cedarsoft.common.collections.fastForEach
import com.cedarsoft.common.kotlin.lang.fromBase64
import com.cedarsoft.common.kotlin.lang.toBase64
import com.cedarsoft.commons.kotlin.bytearray.ByteArrayBuilder
import com.cedarsoft.commons.kotlin.bytearray.ByteArrayReader
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for IntArray2
 */
object IntArray2Serializer : KSerializer<IntArray2> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntArray2", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: IntArray2) {
    encoder.encodeString(toByteArray(value).toBase64())
  }

  override fun deserialize(decoder: Decoder): IntArray2 {
    return parse(decoder.decodeString().fromBase64())
  }

  fun toByteArray(values: IntArray2): ByteArray {
    val builder = ByteArrayBuilder()

    val width = values.width
    val height = values.height

    builder.s16BE(width)
    builder.s16BE(height)

    if (width == 0 || height == 0) {
      //Return immediately - the array is empty
      return builder.toByteArray()
    }

    values.data.fastForEach {
      builder.s32BE(it)
    }

    return builder.toByteArray()
  }

  /**
   * Parses a byte array into a values array
   */
  fun parse(values: ByteArray): IntArray2 {
    val reader = ByteArrayReader(values, 0)

    val width = reader.s16BE()
    val height = reader.s16BE()

    if (width == 0 || height == 0) {
      //Array is empty
      return IntArray2(width, height, 0)
    }

    return IntArray2(width, height) {
      reader.s32BE()
    }
  }

}
