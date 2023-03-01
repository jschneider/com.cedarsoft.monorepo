package it.neckar.open.javafx

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Duration
import org.tbee.javafx.scene.layout.MigPane
import java.util.concurrent.ThreadLocalRandom

class ProgressIndicatorBarDemo : Application() {
  @Throws(Exception::class)
  override fun start(primaryStage: Stage) {
    val root = MigPane("", "[grow, fill]", "[grow, fill]")

    val progressIndicatorBar1 = ProgressIndicatorBar(20.0, "%.1f", "mA")
    root.add(progressIndicatorBar1, "alignx center, aligny center, wrap")

    val progressIndicatorBar2 = ProgressIndicatorBar(100.0, "%.0f", "%")
    root.add(progressIndicatorBar2, "alignx center, aligny center, wrap")

    val progressIndicatorBar3 = ProgressIndicatorBar(-1.0, "%.0f", "%")
    root.add(progressIndicatorBar3, "alignx center, aligny center")

    val timeLine = Timeline(KeyFrame(Duration.millis(50.0), { event: ActionEvent? ->
      val workDone = progressIndicatorBar1.workDone
      if (workDone < 20.0) {
        progressIndicatorBar1.workDone = workDone + 0.1
      } else {
        progressIndicatorBar1.workDone = 4.0
      }
      progressIndicatorBar2.workDone = (ThreadLocalRandom.current().nextInt(0, 100 + 1)).toDouble()
    }))
    timeLine.cycleCount = Animation.INDEFINITE
    timeLine.play()

    primaryStage.scene = Scene(root, 200.0, 100.0)
    primaryStage.show()
  }
}
