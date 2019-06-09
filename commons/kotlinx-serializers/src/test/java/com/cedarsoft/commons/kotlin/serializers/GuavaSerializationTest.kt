package com.cedarsoft.commons.kotlin.serializers

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class GuavaSerializationTest : AbstractSerializationTest() {
  @Test
  internal fun testImmutableList() {
    val obj = MyInnerObject(
      "asdf",
      ImmutableList.of("a", "b", "c"),
      ImmutableSet.of("x", "y", "z"),
      listOf("d", "e", "f")
    )

    verifyJsonRoundTrip(obj, MyInnerObject.serializer())
    verifyProtoBuffRoundTrip(obj, MyInnerObject.serializer())
  }

  @Test
  internal fun testEmptyList() {
    val obj = MyInnerObject(
      "asdf",
      ImmutableList.of(),
      ImmutableSet.of(),
      listOf()
    )

    verifyJsonRoundTrip(obj, MyInnerObject.serializer())
    verifyProtoBuffRoundTrip(obj, MyInnerObject.serializer())
  }

  @Test
  internal fun testMeptyMyInnerSmall() {
    val obj = MyInnerSmall(
      "asdf",
      ImmutableList.of()
    )

    verifyJsonRoundTrip(obj, MyInnerSmall.serializer())
    verifyProtoBuffRoundTrip(obj, MyInnerSmall.serializer())
  }
}


@Serializable
data class MyInnerObject(
  val name: String,
  @Serializable(with = ImmutableListSerializer::class)
  val immutableStrings: ImmutableList<String> = ImmutableList.of(),
  @Serializable(with = ImmutableSetSerializer::class)
  val immutableStringsSet: ImmutableSet<String> = ImmutableSet.of(),
  val stringsList: List<String> = listOf()
)


@Serializable
data class MyInnerSmall(
  val name: String,
  @Serializable(with = ImmutableListSerializer::class)
  val immutableString: ImmutableList<String> = ImmutableList.of()
)
