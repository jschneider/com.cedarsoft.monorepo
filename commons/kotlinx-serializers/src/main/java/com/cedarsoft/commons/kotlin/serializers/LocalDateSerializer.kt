package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Serializer for LocalDate
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
  override val descriptor: SerialDescriptor = StringDescriptor.withName("LocalDate")

  override fun serialize(encoder: Encoder, obj: LocalDate) {
    encoder.encodeString(obj.format(DateTimeFormatter.ISO_DATE))
  }

  override fun deserialize(decoder: Decoder): LocalDate {
    return LocalDate.from(DateTimeFormatter.ISO_DATE.parse(decoder.decodeString()))
  }
}