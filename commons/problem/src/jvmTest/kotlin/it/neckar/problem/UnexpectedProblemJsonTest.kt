package it.neckar.problem

import assertk.*
import assertk.assertions.*
import it.neckar.open.serialization.roundTrip
import org.junit.jupiter.api.Test

/**
 *
 */
class UnexpectedProblemJsonTest {
  @Test
  fun testJsonRoundtrip() {
    val problem = UnexpectedProblem(
      message = "This is a test exception",
      exceptionType = "java.lang.IllegalStateException",
      instanceUri = "the instance uri",
      stackTrace = "the stacktrace"
    )

    roundTrip(
      problem,
      UnexpectedProblem.serializer()
    ) {
      //language=JSON
      """
        {
          "message" : "This is a test exception",
          "exceptionType" : "java.lang.IllegalStateException",
          "instanceUri" : "the instance uri",
          "stackTrace" : "the stacktrace"
        }
      """.trimIndent()
    }
  }

  @Test
  fun testConversion() {
    val exception = IllegalStateException("Test message")
    val problem = exception.toUnexpectedProblem()
      .copy(stackTrace = "the stacktrace")

    roundTrip(
      problem,
      UnexpectedProblem.serializer()
    ) {
      //language=JSON
      """
        {
          "message" : "Test message",
          "exceptionType" : "java.lang.IllegalStateException",
          "instanceUri" : null,
          "stackTrace" : "the stacktrace"
        }
        """.trimIndent()
    }
  }

  @Test
  fun testStackTraceWithConversion() {
    val exception = IllegalStateException("Test message")
    val problem = exception.toUnexpectedProblem()

    assertThat(problem.stackTrace.toString()).contains("java.lang.IllegalStateException: Test message")
    assertThat(problem.stackTrace.toString()).contains("UnexpectedProblemJsonTest")
  }
}
