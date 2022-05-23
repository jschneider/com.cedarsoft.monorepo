package com.cedarsoft.commons.javafx

import com.sun.javafx.tk.Toolkit
import com.sun.scenario.animation.AbstractMasterTimer
import com.sun.scenario.animation.shared.TimerReceiver
import javafx.animation.AnimationTimer

/**
 *
 */
object JavaFxTimerDebug {
  private val masterTimer: AbstractMasterTimer = Toolkit.getToolkit().masterTimer as AbstractMasterTimer
  private val animationTimersField = AbstractMasterTimer::class.java.getDeclaredField("animationTimers").also {
    it.isAccessible = true
  }

  /**
   * Returns the list of [AnimationTimer]s that are currently registered / active
   */
  fun findRegisteredAnimationTimers(): List<AnimationTimer> {
    val timerReceivers = animationTimersField.let {
      it.get(masterTimer) as Array<TimerReceiver?>
    }

    return timerReceivers.filterNotNull().map { timerReceiver ->
      val declaredField = timerReceiver.javaClass.getDeclaredField("this$0")
      declaredField.isAccessible = true
      declaredField.get(timerReceiver) as AnimationTimer
    }
  }
}
