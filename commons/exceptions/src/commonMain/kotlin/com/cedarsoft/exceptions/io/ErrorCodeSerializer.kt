package com.cedarsoft.exceptions.io

import com.cedarsoft.exceptions.ErrorCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 *
 */
class ErrorCodeSerializer : KSerializer<ErrorCode> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ErrorCode", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: ErrorCode) {
    encoder.encodeString(value.format())
  }

  override fun deserialize(decoder: Decoder): ErrorCode {
    return ErrorCode.parse(decoder.decodeString())
  }
}
