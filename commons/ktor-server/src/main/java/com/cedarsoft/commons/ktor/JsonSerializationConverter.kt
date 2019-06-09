package com.cedarsoft.commons.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.io.*
import kotlinx.coroutines.io.jvm.javaio.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class JsonSerializationConverter @UnstableDefault constructor(
  private val json: Json = Json.plain
) : KotlinxSerializationConverter() {
  override suspend fun serialize(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any,
    serializer: KSerializer<Any>
  ): Any? {
    return TextContent(
      text = json.stringify(serializer, value),
      contentType = ContentType.Application.Json.withCharset(context.call.suitableCharset())
    )
  }

  override suspend fun deserialize(
    context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>,
    contentType: ContentType,
    input: ByteReadChannel,
    serializer: KSerializer<Any>
  ): Any? {
    val text = input.toInputStream().reader(contentType.charset() ?: Charsets.UTF_8).readText()
    return json.parse(serializer, text)
  }
}