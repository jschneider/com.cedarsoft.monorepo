package com.cedarsoft.i18n

import com.cedarsoft.commons.serialization.roundTrip
import com.cedarsoft.commons.serialization.roundTripCBOR
import com.cedarsoft.commons.serialization.roundTripProtoBuf
import org.junit.jupiter.api.Test

/**
 *
 */

class TextKeySerializerTest {
  @Test
  fun testTextKey() {
    roundTrip(
      com.cedarsoft.i18n.TextKey("daKey", "daDefaultValue"),
      com.cedarsoft.i18n.TextKey.serializer()
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

  @Test
  fun testCbor() {
    roundTripCBOR(
      com.cedarsoft.i18n.TextKey("daKey", "daDefaultValue"),
      """bf636b65796564614b65796c66616c6c6261636b546578746e646144656661756c7456616c7565ff""".trimIndent(), com.cedarsoft.i18n.TextKey.serializer()
    )
  }

  @Test
  fun testProtoBuf() {
    roundTripProtoBuf(
      com.cedarsoft.i18n.TextKey("daKey", "daDefaultValue"),
      """0a0564614b6579120e646144656661756c7456616c7565""".trimIndent(), com.cedarsoft.i18n.TextKey.serializer()
    )
  }
}
