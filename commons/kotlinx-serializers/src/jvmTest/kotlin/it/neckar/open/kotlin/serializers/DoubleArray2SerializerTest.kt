package it.neckar.open.kotlin.serializers

import it.neckar.open.collections.DoubleArray2
import it.neckar.open.serialization.roundTrip
import org.junit.jupiter.api.Test

class DoubleArray2SerializerTest {
  @Test
  fun testIt() {
    val value = DoubleArray2(10, 8) {
      it * 3.0
    }

    roundTrip(value, DoubleArray2Serializer) {
      """
        "AAoACAAAAAAAAAAAQAgAAAAAAABAGAAAAAAAAEAiAAAAAAAAQCgAAAAAAABALgAAAAAAAEAyAAAAAAAAQDUAAAAAAABAOAAAAAAAAEA7AAAAAAAAQD4AAAAAAABAQIAAAAAAAEBCAAAAAAAAQEOAAAAAAABARQAAAAAAAEBGgAAAAAAAQEgAAAAAAABASYAAAAAAAEBLAAAAAAAAQEyAAAAAAABATgAAAAAAAEBPgAAAAAAAQFCAAAAAAABAUUAAAAAAAEBSAAAAAAAAQFLAAAAAAABAU4AAAAAAAEBUQAAAAAAAQFUAAAAAAABAVcAAAAAAAEBWgAAAAAAAQFdAAAAAAABAWAAAAAAAAEBYwAAAAAAAQFmAAAAAAABAWkAAAAAAAEBbAAAAAAAAQFvAAAAAAABAXIAAAAAAAEBdQAAAAAAAQF4AAAAAAABAXsAAAAAAAEBfgAAAAAAAQGAgAAAAAABAYIAAAAAAAEBg4AAAAAAAQGFAAAAAAABAYaAAAAAAAEBiAAAAAAAAQGJgAAAAAABAYsAAAAAAAEBjIAAAAAAAQGOAAAAAAABAY+AAAAAAAEBkQAAAAAAAQGSgAAAAAABAZQAAAAAAAEBlYAAAAAAAQGXAAAAAAABAZiAAAAAAAEBmgAAAAAAAQGbgAAAAAABAZ0AAAAAAAEBnoAAAAAAAQGgAAAAAAABAaGAAAAAAAEBowAAAAAAAQGkgAAAAAABAaYAAAAAAAEBp4AAAAAAAQGpAAAAAAABAaqAAAAAAAEBrAAAAAAAAQGtgAAAAAABAa8AAAAAAAEBsIAAAAAAAQGyAAAAAAABAbOAAAAAAAEBtQAAAAAAAQG2gAAAAAAA="
      """.trimIndent()
    }
  }
}
