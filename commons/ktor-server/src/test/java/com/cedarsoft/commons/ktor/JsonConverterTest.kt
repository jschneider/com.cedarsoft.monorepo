package com.cedarsoft.commons.ktor

import io.ktor.http.ContentType
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.TestApplicationResponse
import io.ktor.server.testing.setBody
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify

class JsonConverterTest : AbstractKotlinxSerializationConverterTest() {
  override val contentType = ContentType.Application.Json
  override val contentConverter = JsonSerializationConverter()

  @ImplicitReflectionSerializer
  override fun createEntityRequest(entity: Entity, request: TestApplicationRequest) {
    request.setBody(Json.stringify(entity))
  }

  @ImplicitReflectionSerializer
  override fun parseEntityResponse(response: TestApplicationResponse): Entity {
    return Json.parse(response.content!!)
  }

  @ImplicitReflectionSerializer
  override fun createMultiMessageRequest(multiMessage: MultiMessage, request: TestApplicationRequest) {
    request.setBody(Json.stringify(multiMessage))
  }

  @ImplicitReflectionSerializer
  override fun parseMultiMessageResponse(response: TestApplicationResponse): MultiMessage {
    return Json.parse(response.content!!)
  }

  @ImplicitReflectionSerializer
  override fun createMessageGenericRequest(message: MessageString, request: TestApplicationRequest) {
    request.setBody(Json.stringify(MessageString.serializer(), message))
  }

  @ImplicitReflectionSerializer
  override fun parseMessageGenericResponse(response: TestApplicationResponse): MessageString {
    return Json.parse(MessageString.serializer(), response.content!!)
  }
}