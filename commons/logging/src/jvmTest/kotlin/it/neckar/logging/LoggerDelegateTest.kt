package it.neckar.logging

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.Logger

class LoggerDelegateTest {
  val loggerInClass: Logger by LoggerDelegate()

  @Test
  fun testDelegate() {
    assertThat(loggerInCompanionObject.name).isEqualTo("it.neckar.logging.LoggerDelegateTest")
    assertThat(loggerInClass.name).isEqualTo("it.neckar.logging.LoggerDelegateTest")
  }

  companion object {
    val loggerInCompanionObject: Logger by LoggerDelegate()
  }
}
