package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import com.cedarsoft.javafx.test.JavaFxTest
import javafx.animation.Animation.INDEFINITE
import javafx.animation.Animation.Status.RUNNING
import javafx.animation.Animation.Status.STOPPED
import javafx.animation.KeyFrame
import javafx.util.Duration
import org.junit.jupiter.api.Test

@JavaFxTest
class TimelineSupportTest {
  @Test
  fun testDispose() {
    val support = TimelineSupport()
    val timeline = support.createTimeline(KeyFrame(Duration(5000.0), { }))

    timeline.cycleCount = INDEFINITE
    timeline.playFromStart()

    assertThat(timeline.status).isEqualTo(RUNNING)
    support.dispose()
    assertThat(timeline.status).isEqualTo(STOPPED)

  }
}
