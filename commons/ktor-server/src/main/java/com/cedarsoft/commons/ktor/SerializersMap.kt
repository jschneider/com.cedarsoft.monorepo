package com.cedarsoft.commons.ktor

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
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
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Contains serializers for their class
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class SerializersMap {
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

  fun <T : Any> getSerializerNullable(type: KClass<out T>): KSerializer<T>? {
    @Suppress("UNCHECKED_CAST")
    return serializers[type as KClass<Any>] as KSerializer<T>?
  }

  fun <T : Any> getSerializer(type: KClass<out T>): KSerializer<T> {
    return getSerializerNullable(type) ?: throw IllegalStateException("No serializer found for $type")
  }
}
