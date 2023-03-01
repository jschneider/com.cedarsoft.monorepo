package it.neckar.open.exceptions.io

import it.neckar.open.serialization.roundTrip
import it.neckar.open.exceptions.ErrorCode
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
