package it.neckar.open.kotlin.serializers

import it.neckar.open.serialization.roundTrip
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 */
internal class UuidSerializationTest {
  @Test
  internal fun testJson() {
    roundTrip(UUID.fromString("cbf7a43a-ebf0-4598-9413-52a6ad5d2269"), UUIDSerializer) {
      //language=JSON
      """
        "cbf7a43a-ebf0-4598-9413-52a6ad5d2269"
      """.trimIndent()
    }
  }

}
