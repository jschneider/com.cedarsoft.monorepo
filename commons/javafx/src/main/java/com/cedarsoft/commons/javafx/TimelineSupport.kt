package com.cedarsoft.commons.javafx

import com.cedarsoft.common.collections.WeakReference
import com.cedarsoft.common.collections.fastForEach
import com.cedarsoft.dispose.Disposable
import javafx.animation.KeyFrame
import javafx.animation.Timeline

/**
 * Creates and references(!) timelines.
 * Provides a dispose method that stops all timelines
 */
class TimelineSupport : Disposable {
  /**
   * Contains all timelines
   */
  private val timelines = mutableListOf<WeakReference<Timeline>>()

  /**
   * Creates a new timeline that will be stopped when [dispose] is called.
   */
  fun createTimeline(keyFrame: KeyFrame): Timeline {
    return Timeline(keyFrame).also {
      timelines.add(WeakReference(it))
    }
  }

  /**
   * Stops all timelines and clears the [timelines] list
   */
  override fun dispose() {
    timelines.fastForEach {
      it.get()?.stop()
    }

    timelines.clear()
  }
}
