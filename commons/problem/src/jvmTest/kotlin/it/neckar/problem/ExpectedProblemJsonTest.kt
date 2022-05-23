package it.neckar.problem

import com.cedarsoft.commons.serialization.roundTrip
import com.cedarsoft.exceptions.ErrorCode
import org.junit.jupiter.api.Test

/**
 *
 */
class ExpectedProblemJsonTest {
  @Test
  fun testJsonRoundtrip() {
    val expectedProblem = ExpectedProblem(
      errorCode = ErrorCode.create("TEST", 17),
      title = "theTitle",
      status = it.neckar.problem.Status.CONFLICT,
      message = "the details",
      instanceUri = "the instance uri",
      attributes = mapOf(
        "a" to 1.0,
        "b" to "astring",
      ),
      stackTrace = "theStackTrace"
    )

    roundTrip(
      expectedProblem,
      ExpectedProblem.serializer()
    ) {
      //language=JSON
      """
        {
          "errorCode" : "TEST-17",
          "title" : "theTitle",
          "status" : "CONFLICT",
          "message" : "the details",
          "instanceUri" : "the instance uri",
          "attributes" : {
            "a" : 1.0,
            "b" : "astring"
          },
          "stackTrace" : "theStackTrace"
        }
      """.trimIndent()
    }
  }
}
