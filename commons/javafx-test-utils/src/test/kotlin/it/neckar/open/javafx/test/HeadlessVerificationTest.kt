package it.neckar.open.javafx.test

import assertk.*
import assertk.assertions.*
import it.neckar.open.javafx.FxUtils
import javafx.application.Platform
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import java.awt.GraphicsEnvironment
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

    FxUtils.waitFor {
      run.get()
    }
  }
}
