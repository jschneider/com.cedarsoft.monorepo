package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.net.URL

/**
 * Serializer for URL
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@Serializer(forClass = URL::class)
object UrlSerializer : KSerializer<URL> {
  override val descriptor: SerialDescriptor = StringDescriptor.withName("URL")

  override fun serialize(encoder: Encoder, obj: URL) {
    encoder.encodeString(obj.toString())
  }

  override fun deserialize(decoder: Decoder): URL {
    return URL(decoder.decodeString())
  }
}