package com.cedarsoft.commons.kotlin.serializers

import com.google.common.collect.ImmutableList
import kotlinx.serialization.builtins.serializer
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class ImmutableListSerializationTest : AbstractSerializationTest() {
  @Test
  internal fun testJson() {
    verifyJsonRoundTrip(createSample(), ImmutableListSerializer(String.serializer()))
  }

  companion object {
    @JvmStatic
    fun createSample(): ImmutableList<String> {
      return ImmutableList.of("a", "b", "c")
    }
  }
}
