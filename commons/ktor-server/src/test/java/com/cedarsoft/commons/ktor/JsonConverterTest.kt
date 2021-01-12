package com.cedarsoft.commons.ktor

import io.ktor.http.ContentType
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.TestApplicationResponse
import io.ktor.server.testing.setBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonConverterTest : AbstractKotlinxSerializationConverterTest() {
  override val contentType = ContentType.Application.Json
  override val contentConverter = JsonSerializationConverter()

  override fun createEntityRequest(entity: Entity, request: TestApplicationRequest) {
    request.setBody(Json.encodeToString(entity))
  }

  override fun parseEntityResponse(response: TestApplicationResponse): Entity {
    return Json.decodeFromString(Entity.serializer(), response.content!!)
  }

  override fun createMultiMessageRequest(multiMessage: MultiMessage, request: TestApplicationRequest) {
    request.setBody(Json.encodeToString(multiMessage))
  }

  override fun parseMultiMessageResponse(response: TestApplicationResponse): MultiMessage {
    return Json.decodeFromString(MultiMessage.serializer(), response.content!!)
  }

  override fun createMessageGenericRequest(message: MessageString, request: TestApplicationRequest) {
    request.setBody(Json.encodeToString(MessageString.serializer(), message))
  }

  override fun parseMessageGenericResponse(response: TestApplicationResponse): MessageString {
    return Json.decodeFromString(MessageString.serializer(), response.content!!)
  }
}
