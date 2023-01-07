package it.neckar.logging

import assertk.*
import assertk.assertions.*
import kotlin.test.Test

class LoggerJsTest {
  val logger: Logger by LoggerDelegate()

  @Test
  fun testName() {
    assertThat(logger.getName()).isEqualTo("LoggerJsTest")
  }

  @Test
  fun testInstances() {
    assertThat(LoggerFactory.getLogger("A")).isSameAs(LoggerFactory.getLogger("A"))
  }

  @Test
  fun testExtensionMethod() {
    logger.debug { "Only if debug enabled!" }
    logger.info { "Only if info enabled!" }
    logger.warn { "Only if warn enabled!" }
    logger.error { "Only if error enabled!" }

    LogConfigurer.setLogLevel(logger, Level.INFO)
    logger.debug { fail("must not be called") }
    logger.info { "Only if info enabled!" }
    logger.warn { "Only if warn enabled!" }
    logger.error { "Only if error enabled!" }

    LogConfigurer.setLogLevel(logger, Level.WARN)
    logger.debug { fail("must not be called") }
    logger.info { fail("must not be called") }
    logger.warn { "Only if warn enabled!" }
    logger.error { "Only if error enabled!" }

    LogConfigurer.setLogLevel(logger, Level.ERROR)
    logger.debug { fail("must not be called") }
    logger.info { fail("must not be called") }
    logger.warn { fail("must not be called") }
    logger.error { "Only if error enabled!" }
  }
}
