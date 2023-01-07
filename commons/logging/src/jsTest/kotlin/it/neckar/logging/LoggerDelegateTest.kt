package it.neckar.logging

import assertk.*
import assertk.assertions.*
import kotlin.test.Test


class LoggerDelegateTest {
  val loggerInClass: Logger by LoggerDelegate()

  @Test
  fun name() {
    assertThat(loggerInClass.name).isEqualTo("LoggerDelegateTest")
    assertThat(loggerInCompanionObject.name).isEqualTo("Companion")
  }

  companion object {
    val loggerInCompanionObject: Logger by LoggerDelegate()
  }
}

