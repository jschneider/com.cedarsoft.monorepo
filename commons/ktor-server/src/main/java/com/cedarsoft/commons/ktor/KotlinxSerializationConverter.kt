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
import kotlinx.serialization.internal.BooleanSerializer
import kotlinx.serialization.internal.ByteSerializer
import kotlinx.serialization.internal.CharSerializer
import kotlinx.serialization.internal.DoubleSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.internal.IntSerializer
import kotlinx.serialization.internal.LongSerializer
import kotlinx.serialization.internal.ShortSerializer
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.internal.UnitSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.collections.set
import kotlin.reflect.KClass

/**
 * Supports kotlinx serialization
 */
abstract class KotlinxSerializationConverter : ContentConverter {
  @Suppress("UNCHECKED_CAST")
  private val serializers = mutableMapOf(
    Unit::class as KClass<Any> to UnitSerializer as KSerializer<Any>,
    Boolean::class as KClass<Any> to BooleanSerializer as KSerializer<Any>,
    Byte::class as KClass<Any> to ByteSerializer as KSerializer<Any>,
    Short::class as KClass<Any> to ShortSerializer as KSerializer<Any>,
    Int::class as KClass<Any> to IntSerializer as KSerializer<Any>,
    Long::class as KClass<Any> to LongSerializer as KSerializer<Any>,
    Float::class as KClass<Any> to FloatSerializer as KSerializer<Any>,
    Double::class as KClass<Any> to DoubleSerializer as KSerializer<Any>,
    Char::class as KClass<Any> to CharSerializer as KSerializer<Any>,
    String::class as KClass<Any> to StringSerializer as KSerializer<Any>
  )

  fun <T : Any> register(type: KClass<T>, serializer: KSerializer<T>) {
    @Suppress("UNCHECKED_CAST")
    serializers[type as KClass<Any>] = serializer as KSerializer<Any>
  }

  inline fun <reified T : Any> register(serializer: KSerializer<T>) {
    register(T::class, serializer)
  }

  @ImplicitReflectionSerializer
  inline fun <reified T : Any> register() {
    register(T::class.serializer())
  }

  private fun getSerializer(type: KClass<*>): KSerializer<Any>? {
    return serializers[type]
  }

  override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
    val subject = context.subject
    val input = subject.value as? ByteReadChannel ?: return null
    val serializer = getSerializer(subject.type) ?: return null
    val contentType = context.call.request.contentType()

    return deserialize(context, contentType, input, serializer)
  }

  override suspend fun convertForSend(
    context: PipelineContext<Any, ApplicationCall>,
    contentType: ContentType,
    value: Any
  ): Any? {
    val serializer = getSerializer(value::class) ?: return null
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
  if (json != null) {
    register(ContentType.Application.Json, JsonSerializationConverter(json).apply(block))
  }

  if (protoBuf != null) {
    register(ContentType.Application.ProtoBuf, ProtoBufSerializationConverter(protoBuf).apply(block))
  }
}