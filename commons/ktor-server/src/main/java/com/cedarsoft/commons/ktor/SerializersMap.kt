package com.cedarsoft.commons.ktor

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Contains serializers for their class
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class SerializersMap {
  @Suppress("UNCHECKED_CAST")
  private val serializers: MutableMap<KClass<Any>, KSerializer<out Any>> = mutableMapOf(
    Boolean::class as KClass<Any> to Boolean.serializer(),
    Byte::class as KClass<Any> to Byte.serializer(),
    Short::class as KClass<Any> to Short.serializer(),
    Int::class as KClass<Any> to Int.serializer(),
    Long::class as KClass<Any> to Long.serializer(),
    Float::class as KClass<Any> to Float.serializer(),
    Double::class as KClass<Any> to Double.serializer(),
    Char::class as KClass<Any> to Char.serializer(),
    String::class as KClass<Any> to String.serializer()
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
