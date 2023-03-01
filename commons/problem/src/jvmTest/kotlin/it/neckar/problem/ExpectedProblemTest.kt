package it.neckar.problem

import assertk.*
import assertk.assertions.*
import it.neckar.open.serialization.roundTrip
import it.neckar.open.exceptions.ApplicationException
import it.neckar.open.exceptions.ErrorCode
import it.neckar.open.i18n.I18nConfiguration
import org.junit.jupiter.api.Test

class ExpectedProblemTest {
  @Test
  fun testIt() {
    val expectedProblem = ExpectedProblem(
      errorCode = ErrorCode.create("TEST", 17),
      "the title",
      Status.FORBIDDEN,
      "the details",
      "theInstanceUri",
      emptyMap(),
      stackTrace = "thisIsTheStackTrace",
    )

    assertThat(expectedProblem).isNotNull()
  }

  @Test
  fun testAppException() {
    val expectedProblem = MyAppException(MyAppException.MyApp.Err1)
      .toExpectedProblem(i18nConfiguration = I18nConfiguration.US)
      .copy(stackTrace = "TheStackTrace")

    roundTrip(expectedProblem) {
      //language=JSON
      """
        {
          "errorCode" : "MyApp-17",
          "title" : "title for Err1",
          "status" : "BAD_REQUEST",
          "message" : "message for Err1",
          "instanceUri" : null,
          "attributes" : null,
          "stackTrace" : "TheStackTrace"}
      """.trimIndent()
    }
  }
}

class MyAppException(details: MyApp, parameters: Map<String, Any>? = null) : ApplicationException(details, parameters) {
  enum class MyApp(val index: Int) : Details {
    Err1(17),
    Err2(18), ;

    override val errorCode: ErrorCode = ErrorCode(ErrorCode.Prefix("MyApp"), index)

    override fun getTitle(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return "title for $name"
    }

    override fun getLocalizedMessage(i18nConfiguration: I18nConfiguration, parameters: Map<String, Any>?): String {
      return "message for $name"
    }
  }
}
