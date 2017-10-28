package com.cedarsoft.exceptions.handling.notification

import com.sun.javafx.stage.StageHelper
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main(args: Array<String>) {
  launch(FxBalloonDialogDemo::class.java)
}

internal class FxBalloonDialogDemo : Application() {
  override fun start(primaryStage: Stage) {
    val notificationService = FxNotificationService()

    primaryStage.scene = Scene(VBox())
    primaryStage.width = 1200.0
    primaryStage.height = 1200.0

    primaryStage.show()
    primaryStage.centerOnScreen()

    val stages = StageHelper.getStages()
    for (stage in stages) {
      println("stage --> ${stage}")
    }

    val notification = Notification("daTitle",
      "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.\nasdf asdf sadf fsda fasd asdf sadf asdf\n\naslkjawl4kjasldkfj",
      DetailsCallback { notification -> println("--> Details clicked <$notification>") })
    notificationService.showNotification(notification)
  }
}
