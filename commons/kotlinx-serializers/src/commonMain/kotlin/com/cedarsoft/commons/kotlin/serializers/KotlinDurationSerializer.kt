package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration

/**
 * Serializer for Duration.
 *
 * Use like this:
 * ```
 * @file: UseSerializers(KotlinDurationSerializer::class)
 * ```
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = Duration::class)
object KotlinDurationSerializer : KSerializer<Duration> {
  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor(Duration::class.simpleName!!, PrimitiveKind.LONG)

  override fun serialize(encoder: Encoder, value: Duration) {
    encoder.encodeLong(value.inWholeNanoseconds)
  }

  override fun deserialize(decoder: Decoder): Duration {
    val decodeLong = decoder.decodeLong()
    return Duration.nanoseconds(decodeLong)
  }
}
