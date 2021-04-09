package com.cedarsoft.javafx.test

import assertk.*
import assertk.assertions.*
import javafx.application.Platform
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import java.awt.GraphicsEnvironment
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


/**
 *
 */
@ExtendWith(ApplicationExtension::class)
class HeadlessVerificationTest {
  @Test
  fun testIt() {
    assertThat(GraphicsEnvironment.isHeadless(), "must not be run in headless mode").isFalse()
  }

  @Test
  fun testCreateJavaFX() {
    assertThat(Platform.isFxApplicationThread()).isFalse()

    val run = AtomicBoolean()
    Platform.runLater {
      run.set(true)
    }

    Awaitility.await().atMost(30, TimeUnit.SECONDS).untilTrue(run)
  }
}
