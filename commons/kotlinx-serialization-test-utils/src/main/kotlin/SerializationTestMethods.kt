package com.cedarsoft.commons.serialization

import com.cedarsoft.common.kotlin.lang.hex
import com.cedarsoft.test.utils.JsonUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

/**
 * Tests serialization round trip
 */
inline fun <reified T> roundTrip(objectToSerialize: T, serializer: KSerializer<T> = serializer(), serializersModule: SerializersModule = EmptySerializersModule, expectedJsonProvider: () -> String?) {
  roundTrip(objectToSerialize, serializer, serializersModule, expectedJsonProvider())
}

/**
 * Tests the round trip. If the [expectedJson] is null, the resulting JSON will not be checked
 */
fun <T> roundTrip(objectToSerialize: T, serializer: KSerializer<T>, serializersModule: SerializersModule = EmptySerializersModule, expectedJson: String?) {
  val encoder = Json {
    this.serializersModule = serializersModule
    prettyPrint = false
  }

  val json = encoder.encodeToString(serializer, objectToSerialize)

  //println("JSON length: ${json.toByteArray().size}")

  expectedJson?.let {
    JsonUtils.assertJsonEquals(expectedJson, json)
  }

  val deserialized = encoder.decodeFromString(serializer, json)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

fun <T> roundTripCBOR(objectToSerialize: T, expectedHex: String?, serializer: KSerializer<T>) {
  val bytes = Cbor.encodeToByteArray(serializer, objectToSerialize)

  //println("CBOR length: ${bytes.size}")
  if (expectedHex != null) {
    assertThat(bytes.hex).isEqualTo(expectedHex)
  }

  val deserialized = Cbor.decodeFromByteArray(serializer, bytes)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

fun <T> roundTripProtoBuf(objectToSerialize: T, expectedHex: String?, serializer: KSerializer<T>) {
  val protoBuf = ProtoBuf {
  }

  val bytes = protoBuf.encodeToByteArray(serializer, objectToSerialize)

  //println("ProtoBuf length: ${bytes.size}")
  if (expectedHex != null) {
    assertThat(bytes.hex).isEqualTo(expectedHex)
  }

  val deserialized = protoBuf.decodeFromByteArray(serializer, bytes)
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

