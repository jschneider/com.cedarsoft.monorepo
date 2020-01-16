package com.cedarsoft.commons.ktor

import io.ktor.application.ApplicationCall
import io.ktor.content.ByteArrayContent
import io.ktor.http.ContentType
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.util.cio.toByteArray
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf

val ContentType.Application.ProtoBuf: ContentType
  get() = ContentType("application", "protobuf")

/**
 * Serializer that converts to protocol buffers
 */
class ProtoBufSerializationConverter(private val protoBuf: ProtoBuf = ProtoBuf.plain) : KotlinxSerializationConverter() {
  override suspend fun deserialize(
    context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>,
    contentType: ContentType,
    input: ByteReadChannel,
    serializer: KSerializer<Any>
  ): Any? {
    val bytes = input.toByteArray()
    return protoBuf.load(serializer, bytes)
  }

  override suspend fun serialize(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any,
    serializer: KSerializer<Any>
  ): Any? {
    return ByteArrayContent(
      bytes = protoBuf.dump(serializer, value),
      contentType = ContentType.Application.ProtoBuf
    )
  }
}
