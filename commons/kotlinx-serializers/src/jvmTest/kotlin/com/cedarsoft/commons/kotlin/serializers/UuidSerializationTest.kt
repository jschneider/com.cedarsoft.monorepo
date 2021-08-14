package com.cedarsoft.commons.kotlin.serializers

import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class UuidSerializationTest : AbstractSerializationTest() {
  @Test
  internal fun testJson() {
    verifyJsonRoundTrip(createSample(), UUIDSerializer)
  }

  companion object {
    @JvmStatic
    fun createSample(): UUID {
      return UUID.fromString("cbf7a43a-ebf0-4598-9413-52a6ad5d2269")
    }
  }
}
