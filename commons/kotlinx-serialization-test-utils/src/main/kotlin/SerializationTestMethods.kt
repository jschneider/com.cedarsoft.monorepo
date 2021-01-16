package com.cedarsoft.commons.serialization

import com.cedarsoft.test.utils.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
fun <T> roundTrip(objectToSerialize: T, expectedJson: String, serializer: KSerializer<T>) {
  val encoder = Json {
    prettyPrint = false
  }

  val json = encoder.encodeToString(serializer, objectToSerialize)

  println("JSON length: ${json.toByteArray().size}")

  JsonUtils.assertJsonEquals(expectedJson, json)

  val deserialized = Json.decodeFromString(serializer, json)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

fun <T> roundTripCBOR(objectToSerialize: T, expectedHex: String, serializer: KSerializer<T>) {
  val bytes = Cbor.encodeToByteArray(serializer, objectToSerialize)

  println("CBOR length: ${bytes.size}")
  //assertThat(bytes.hex).isEqualTo(expectedHex)

  val deserialized = Cbor.decodeFromByteArray(serializer, bytes)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

fun <T> roundTripProtoBuf(objectToSerialize: T, expectedHex: String, serializer: KSerializer<T>) {
  val protoBuf = ProtoBuf {
  }

  val bytes = protoBuf.encodeToByteArray(serializer, objectToSerialize)

  println("ProtoBuf length: ${bytes.size}")
  //assertThat(bytes.hex).isEqualTo(expectedHex)

  val deserialized = protoBuf.decodeFromByteArray(serializer, bytes)
  assertThat(deserialized).isEqualTo(objectToSerialize)
}

/**
 * Serializes a list of objects
 */
fun <T> roundTripList(vararg objectsToSerialize: T, expectedJson: String, serializer: KSerializer<T>) {
  val encoder = Json {
    prettyPrint = false
  }

  val listSerializer = ListSerializer(serializer)

  val objectsToSerializeList: List<T> = objectsToSerialize.toList()
  val json = encoder.encodeToString(listSerializer, objectsToSerializeList)

  println("JSON length: ${json.toByteArray().size}")

  JsonUtils.assertJsonEquals(expectedJson, json)

  val deserialized = Json.decodeFromString(listSerializer, json)
  assertThat(deserialized).isEqualTo(objectsToSerializeList)
}

