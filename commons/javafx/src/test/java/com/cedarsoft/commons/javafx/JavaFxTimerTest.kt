package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import com.cedarsoft.javafx.test.JavaFxTest
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import jfxtras.util.PlatformUtil
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.time.milliseconds

@JavaFxTest
class JavaFxTimerTest {
  @Test
  fun testDelay() {
    assertThat(Platform.isFxApplicationThread()).isFalse()

    var called = false

    JavaFxTimer.delay(0.milliseconds) {
      assertThat(called).isFalse()
      called = true
    }

    PlatformUtil.runAndWait {
      //wait to ensure the event has been processed
    }

    Awaitility.await()
      .pollDelay(10, TimeUnit.MILLISECONDS)
      .until {
        called
      }
  }

  /**
   * Shows that a timeline with only one key frame at ZERO does *not* run at all
   */
  @Test
  fun testEmptyTimeline() {
    val eventHandler: EventHandler<ActionEvent> = EventHandler { throw IllegalStateException("Should not be called") }
    val keyFrame = KeyFrame(javafx.util.Duration.ZERO, eventHandler)
    val timeline = Timeline(keyFrame)

    assertThat(timeline.status).isEqualTo(Animation.Status.STOPPED)
    timeline.play()
    assertThat(timeline.status).isEqualTo(Animation.Status.STOPPED)

    PlatformUtil.runAndWait {
      //wait to ensure the event has been processed
    }

    assertThat(timeline.status).isEqualTo(Animation.Status.STOPPED)
  }

  @Test
  fun testEmptyTimeLine() {
    val timeline = Timeline()

    assertThat(timeline.status).isEqualTo(Animation.Status.STOPPED)
    timeline.play()
    assertThat(timeline.status).isEqualTo(Animation.Status.STOPPED)
  }
}
