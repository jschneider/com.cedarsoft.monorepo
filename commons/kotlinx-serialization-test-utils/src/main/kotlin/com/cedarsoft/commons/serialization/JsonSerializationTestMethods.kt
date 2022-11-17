package com.cedarsoft.commons.serialization

import com.cedarsoft.test.utils.JsonUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

/**
 * Tests serialization round trip
 */
inline fun <reified T> roundTrip(objectToSerialize: T, serializer: KSerializer<T>, serializersModule: SerializersModule = EmptySerializersModule(), expectedJson: String?) {
  roundTrip(objectToSerialize, serializer, serializersModule) { expectedJson }
}

/**
 * Tests the round trip. If the [expectedJson] is null, the resulting JSON will not be checked
 */
inline fun <reified T> roundTrip(objectToSerialize: T, serializer: KSerializer<T> = serializer(), serializersModule: SerializersModule = EmptySerializersModule(), expectedJsonProvider: () -> String?) {
  val encoder: Json = Json {
    this.serializersModule = serializersModule
    prettyPrint = false
  }

  return roundTrip(objectToSerialize, serializer, encoder, expectedJsonProvider)
}

inline fun <reified T> roundTrip(objectToSerialize: T, serializer: KSerializer<T> = serializer(), encoder: Json, expectedJson: String?) {
  return roundTrip(objectToSerialize, serializer, encoder) { expectedJson }
}

inline fun <reified T> roundTrip(objectToSerialize: T, serializer: KSerializer<T> = serializer(), encoder: Json, expectedJsonProvider: () -> String?) {
  val json = encoder.encodeToString(serializer, objectToSerialize)

  //println("JSON length: ${json.toByteArray().size}")

  expectedJsonProvider()?.let { expectedJson ->
    JsonUtils.assertJsonEquals(expectedJson, json)
  }

  val deserialized = encoder.decodeFromString(serializer, json)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

/**
 * Serializes a list of objects
 */
fun <T> roundTripList(vararg objectsToSerialize: T, expectedJson: String?, serializer: KSerializer<T>) {
  val encoder = Json {
    prettyPrint = false
  }

  val listSerializer = ListSerializer(serializer)

  val objectsToSerializeList: List<T> = objectsToSerialize.toList()
  val json = encoder.encodeToString(listSerializer, objectsToSerializeList)

  //println("JSON length: ${json.toByteArray().size}")

  if (expectedJson != null) {
    JsonUtils.assertJsonEquals(expectedJson, json)
  }

  val deserialized = Json.decodeFromString(listSerializer, json)
  assertThat(deserialized).isEqualTo(objectsToSerializeList)
}

