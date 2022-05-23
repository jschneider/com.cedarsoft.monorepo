package com.cedarsoft.exceptions.io

import com.cedarsoft.commons.serialization.roundTrip
import com.cedarsoft.exceptions.ErrorCode
import org.junit.jupiter.api.Test

class ErrorCodeSerializerTest {
  @Test
  fun testIt() {
    roundTrip(ErrorCode.create("ThePrefix", 11), ErrorCodeSerializer()) {
      //language=JSON
      """
        "ThePrefix-11"
      """.trimIndent()
    }
  }
}
