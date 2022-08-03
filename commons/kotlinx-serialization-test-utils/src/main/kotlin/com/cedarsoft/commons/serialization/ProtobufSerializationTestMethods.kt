package com.cedarsoft.commons.serialization

import com.cedarsoft.common.kotlin.lang.hex
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat


//fun <T> roundTripCBOR(objectToSerialize: T, expectedHex: String?, serializer: KSerializer<T>) {
//  val bytes = Cbor.encodeToByteArray(serializer, objectToSerialize)
//
//  //println("CBOR length: ${bytes.size}")
//  if (expectedHex != null) {
//    assertThat(bytes.hex).isEqualTo(expectedHex)
//  }
//
//  val deserialized = Cbor.decodeFromByteArray(serializer, bytes)
//  assertThat(deserialized).isEqualTo(objectToSerialize)
//}

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

