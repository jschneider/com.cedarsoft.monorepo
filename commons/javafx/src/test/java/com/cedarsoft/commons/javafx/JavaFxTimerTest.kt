package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.time.VirtualNowProvider
import com.cedarsoft.javafx.test.JavaFxTest
import com.cedarsoft.test.utils.VirtualTime
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import jfxtras.util.PlatformUtil
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

@JavaFxTest
class JavaFxTimerTest {

  @VirtualTime(5000.0)
  @Test
  fun testSimple(nowProvider: VirtualNowProvider) {
    assertThat(nowProvider).isInstanceOf(VirtualNowProvider::class)

    assertThat(Platform.isFxApplicationThread()).isFalse()

    var called = false
    JavaFxTimer.delay(10.milliseconds) {
      assertThat(called).isFalse()
      called = true
    }

    nowProvider.add(20.0)
    assertThat(nowProvider.nowMillis()).isEqualTo(5020.0)

    assertThat(called).isFalse()
    JavaFxTimer.waitForPaintPulse()
    Awaitility.await().atMost(30, TimeUnit.SECONDS)
      .pollDelay(10, TimeUnit.MILLISECONDS)
      .until {
        called
      }
    assertThat(called).isTrue()
  }

  @VirtualTime(5000.0)
  @Test
  fun testDelayNotImmediately(nowProvider: VirtualNowProvider) {
    assertThat(Platform.isFxApplicationThread()).isFalse()

    var called = false

    JavaFxTimer.delay(10.milliseconds) {
      assertThat(called).isFalse()
      called = true
    }

    assertThat(called).isFalse()
    JavaFxTimer.waitForPaintPulse()
    assertThat(called).isFalse()

    nowProvider.add(9.0)

    assertThat(called).isFalse()
    JavaFxTimer.waitForPaintPulse()
    assertThat(called).isFalse()

    nowProvider.add(1.0)
    assertThat(nowProvider.nowMillis()).isEqualTo(5010.0)

    assertThat(called).isFalse()
    JavaFxTimer.waitForPaintPulse()
    Awaitility.await().atMost(30, TimeUnit.SECONDS)
      .pollDelay(10, TimeUnit.MILLISECONDS)
      .until {
        called
      }
    assertThat(called).isTrue()
  }

  @Test
  fun testDelay() {
    assertThat(Platform.isFxApplicationThread()).isFalse()

    var called = false

    JavaFxTimer.delay(0.milliseconds) {
      assertThat(called).isFalse()
      called = true
    }

    //wait to ensure the event has been processed
    JavaFxTimer.waitForPaintPulse()

    Awaitility.await().atMost(30, TimeUnit.SECONDS)
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
