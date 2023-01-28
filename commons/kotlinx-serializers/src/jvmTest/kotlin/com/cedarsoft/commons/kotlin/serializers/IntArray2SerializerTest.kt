package com.cedarsoft.commons.kotlin.serializers

import com.cedarsoft.common.collections.IntArray2
import com.cedarsoft.commons.serialization.roundTrip
import org.junit.jupiter.api.Test

class IntArray2SerializerTest {
  @Test
  fun testIt() {
    val value = IntArray2(10, 8) {
      it * 3
    }

    roundTrip(value, IntArray2Serializer) {
      """
        "AAoACAAAAAAAAAADAAAABgAAAAkAAAAMAAAADwAAABIAAAAVAAAAGAAAABsAAAAeAAAAIQAAACQAAAAnAAAAKgAAAC0AAAAwAAAAMwAAADYAAAA5AAAAPAAAAD8AAABCAAAARQAAAEgAAABLAAAATgAAAFEAAABUAAAAVwAAAFoAAABdAAAAYAAAAGMAAABmAAAAaQAAAGwAAABvAAAAcgAAAHUAAAB4AAAAewAAAH4AAACBAAAAhAAAAIcAAACKAAAAjQAAAJAAAACTAAAAlgAAAJkAAACcAAAAnwAAAKIAAAClAAAAqAAAAKsAAACuAAAAsQAAALQAAAC3AAAAugAAAL0AAADAAAAAwwAAAMYAAADJAAAAzAAAAM8AAADSAAAA1QAAANgAAADbAAAA3gAAAOEAAADkAAAA5wAAAOoAAADt"
      """.trimIndent()
    }
  }
}
