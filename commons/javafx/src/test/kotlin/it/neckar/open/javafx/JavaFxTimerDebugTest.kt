package it.neckar.open.javafx

import assertk.*
import assertk.assertions.*
import it.neckar.open.javafx.test.JavaFxTest
import javafx.animation.AnimationTimer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
@JavaFxTest
class JavaFxTimerDebugTest {
  @Test
  fun testGetReflection() {
    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).isEmpty()

    val animationTimer = MyDemoAnimationTimer()

    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).hasSize(0)

    animationTimer.start()


    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).hasSize(1)
    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).contains(animationTimer)

    animationTimer.stop()

    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).hasSize(0)
    assertThat(JavaFxTimerDebug.findRegisteredAnimationTimers()).containsNone(animationTimer)
  }
}

class MyDemoAnimationTimer : AnimationTimer() {
  override fun handle(now: Long) {
  }
}
