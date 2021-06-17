package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions

open class AbstractSerializationTest {
  protected fun <T> verifyAll(objectToSerialize: T, serializer: KSerializer<T>) {
    verifyJsonRoundTrip(objectToSerialize, serializer)
    //verifyProtoBuffRoundTrip(objectToSerialize, serializer)
  }

  protected fun <T> verifyJsonRoundTrip(objectToSerialize: T, serializer: KSerializer<T>) {
    val json = Json.encodeToString(serializer, objectToSerialize)
    val read = Json.decodeFromString(serializer, json)
    Assertions.assertThat(read).isEqualTo(objectToSerialize)
  }

  //protected fun <T> verifyProtoBuffRoundTrip(objectToSerialize: T, serializer: KSerializer<T>) {
  //  val binary = ProtoBuf.dump(serializer, objectToSerialize)
  //  val read = ProtoBuf.load(serializer, binary)
  //  Assertions.assertThat(read).isEqualTo(objectToSerialize)
  //}
}
