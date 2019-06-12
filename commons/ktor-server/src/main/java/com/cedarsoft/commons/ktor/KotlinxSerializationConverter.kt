package com.cedarsoft.commons.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.ContentConverter
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentType
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.io.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Supports kotlinx serialization
 */
abstract class KotlinxSerializationConverter(
  val serializersMap: SerializersMap = SerializersMap()
) : ContentConverter {

  fun <T : Any> register(type: KClass<T>, serializer: KSerializer<T>) {
    return serializersMap.register(type, serializer)
  }

  inline fun <reified T : Any> register(serializer: KSerializer<T>) {
    register(T::class, serializer)
  }

  @ImplicitReflectionSerializer
  inline fun <reified T : Any> register() {
    register(T::class.serializer())
  }

  fun <T : Any> getSerializer(type: KClass<out T>): KSerializer<T> {
    return serializersMap.getSerializer(type)
  }

  override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
    val subject = context.subject
    val input = subject.value as? ByteReadChannel ?: return null

    val type = subject.type
    val serializer = getSerializer(type)
    val contentType = context.call.request.contentType()

    return deserialize(context, contentType, input, serializer)
  }

  override suspend fun convertForSend(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any
  ): Any? {
    val serializer = getSerializer(value::class)
    return serialize(context, contentType, value, serializer)
  }

  protected abstract suspend fun deserialize(
    context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>,
    contentType: ContentType,
    input: ByteReadChannel,
    serializer: KSerializer<Any>
  ): Any?

  protected abstract suspend fun serialize(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any,
    serializer: KSerializer<Any>
  ): Any?
}

/**
 * Registers the serializable converters for json and proto buf if provided.
 */
@UnstableDefault
inline fun ContentNegotiation.Configuration.kotlinxSerialization(
  json: Json? = Json.plain,
  protoBuf: ProtoBuf? = null,
  block: KotlinxSerializationConverter.() -> Unit
) {
  val jsonSerializationConverter: JsonSerializationConverter? = json?.let { JsonSerializationConverter(it) }
  val protoBufSerializationConverter: ProtoBufSerializationConverter? = protoBuf?.let { ProtoBufSerializationConverter(it) }

  kotlinxSerialization(jsonSerializationConverter, protoBufSerializationConverter, block)
}

@UnstableDefault
inline fun ContentNegotiation.Configuration.kotlinxSerialization(
  jsonConverter: JsonSerializationConverter? = null,
  protoBufConverter: ProtoBufSerializationConverter? = null,
  block: KotlinxSerializationConverter.() -> Unit
) {
  if (jsonConverter != null) {
    register(ContentType.Application.Json, jsonConverter.apply(block))
  }

  if (protoBufConverter != null) {
    register(ContentType.Application.ProtoBuf, protoBufConverter.apply(block))
  }
}
