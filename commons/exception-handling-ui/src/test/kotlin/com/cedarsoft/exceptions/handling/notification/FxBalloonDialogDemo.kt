package it.neckar.open.exceptions.handling.notification

import it.neckar.open.javafx.FxUtils
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

/**
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

    val stages = FxUtils.stages
    for (stage in stages) {
      println("stage --> ${stage}")
    }

    val notification = Notification("daTitle",
                                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.\nasdf asdf sadf fsda fasd asdf sadfa dfafs dfasd af sddasf afsd asdf asfasdfasf dfasdfasd  asdf\n\naslkjawl4kjasldkfj",
                                    object : DetailsCallback {
                                      override fun detailsClicked(notification: Notification) {
                                        println("--> Details clicked <$notification>")
                                      }
                                    })
    notificationService.showNotification(notification)
  }
}
