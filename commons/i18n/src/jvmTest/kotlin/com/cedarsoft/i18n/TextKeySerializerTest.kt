package com.cedarsoft.i18n

import com.cedarsoft.commons.serialization.roundTrip
import org.junit.jupiter.api.Test

/**
 *
 */

class TextKeySerializerTest {
  @Test
  fun testTextKey() {
    roundTrip(
      TextKey("daKey", "daDefaultValue"),
      TextKey.serializer()
    ) {
      //language=JSON
      """
      {
        "key" : "daKey",
        "fallbackText" : "daDefaultValue"
      }
    """.trimIndent()
    }
  }
}
