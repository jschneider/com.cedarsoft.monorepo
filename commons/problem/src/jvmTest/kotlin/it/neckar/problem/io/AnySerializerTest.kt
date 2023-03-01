package it.neckar.problem.io

import it.neckar.open.serialization.roundTrip
import org.junit.jupiter.api.Test

class AnySerializerTest {
  @Test
  fun testAny() {

    roundTrip("asdf", AnySerializer()) {
      //language=JSON
      """
        "asdf"
      """
    }

    roundTrip(8, AnySerializer()) {
      //language=JSON
      """
        8
      """
    }

    roundTrip(7.2, AnySerializer()) {
      //language=JSON
      """
        7.2
      """
    }

    roundTrip(true, AnySerializer()) {
      //language=JSON
      """
        true
      """
    }

    roundTrip(null, AnySerializer()) {
      //language=JSON
      """
        null
      """
    }
  }
}
