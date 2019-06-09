package com.cedarsoft.commons.ktor

import io.ktor.http.ContentType
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.TestApplicationResponse
import io.ktor.server.testing.setBody
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.dump
import kotlinx.serialization.load
import kotlinx.serialization.protobuf.ProtoBuf

@ImplicitReflectionSerializer
class ProtBufConverterTest : AbstractKotlinxSerializationConverterTest() {
  override val contentType: ContentType = ContentType.Application.ProtoBuf
  override val contentConverter: ProtoBufSerializationConverter = ProtoBufSerializationConverter()

  override fun createEntityRequest(entity: Entity, request: TestApplicationRequest) {
    request.setBody(ProtoBuf.dump(entity))
  }

  override fun parseEntityResponse(response: TestApplicationResponse): Entity {
    return ProtoBuf.load(response.byteContent!!)
  }

  override fun createMultiMessageRequest(multiMessage: MultiMessage, request: TestApplicationRequest) {
    request.setBody(ProtoBuf.dump(multiMessage))
  }

  override fun parseMultiMessageResponse(response: TestApplicationResponse): MultiMessage {
    return ProtoBuf.load(response.byteContent!!)
  }

  override fun createMessageGenericRequest(message: MessageString, request: TestApplicationRequest) {
    request.setBody(ProtoBuf.dump(MessageString.serializer(), message))
  }

  override fun parseMessageGenericResponse(response: TestApplicationResponse): MessageString {
    return ProtoBuf.load(MessageString.serializer(), response.byteContent!!)
  }
}