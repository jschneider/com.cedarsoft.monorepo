package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.time.VirtualNowProvider
import com.cedarsoft.concurrent.ThreadDeadlockDetector
import com.cedarsoft.javafx.test.JavaFxTest
import com.cedarsoft.test.utils.VirtualTime
import com.cedarsoft.test.utils.isFalse
import com.cedarsoft.test.utils.isTrue
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import jfxtras.util.PlatformUtil
import org.awaitility.core.ConditionTimeoutException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

@JavaFxTest
class JavaFxTimerTest {

  private lateinit var threadDeadlockDetector: ThreadDeadlockDetector

  @BeforeEach
  fun setUp() {
    threadDeadlockDetector = ThreadDeadlockDetector(1_000).also { it.start() }
  }

  @AfterEach
  fun tearDown() {
    threadDeadlockDetector.stop()
  }

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

    try {
      JavaFxTimer.waitForPaintPulse()
      FxUtils.waitFor {
        called
      }
      assertThat(called).isTrue()
    } catch (e: ConditionTimeoutException) {
      println("ConditionTimeoutException caught!!!")
      Thread.getAllStackTraces().forEach { thread, stackTrace ->
        println("--------- ${thread.id} - ${thread.name}-------------------")
        stackTrace.forEach {
          println(it.toString())
        }
      }

      throw e
    }
  }

  @VirtualTime(5000.0)
  @Test
  fun testDelayNotImmediately(nowProvider: VirtualNowProvider) {
    try {
      assertThat(Platform.isFxApplicationThread()).isFalse()

      val called = AtomicBoolean(false)

      JavaFxTimer.delay(10.milliseconds) {
        assertThat(called).isFalse()
        called.set(true)
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

      JavaFxTimer.waitForPaintPulse()
      FxUtils.waitFor { called.get() }
      assertThat(called).isTrue()
    } catch (e: ConditionTimeoutException) {
      println("---------- ConditionTimeoutException ----------")
      Thread.dumpStack()

      Thread.getAllStackTraces().forEach {
        println("---- ${it.key?.name} --------")
        println(it.value.joinToString("\n"))
      }
    }
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

    FxUtils.waitFor {
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
