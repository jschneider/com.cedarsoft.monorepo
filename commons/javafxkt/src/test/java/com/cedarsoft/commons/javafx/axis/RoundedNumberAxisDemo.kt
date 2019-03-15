package com.cedarsoft.commons.javafx.axis

import com.cedarsoft.commons.javafx.Components
import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main() {
  Application.launch(RoundedNumberAxisDemo::class.java)
}

internal class RoundedNumberAxisDemo : Application() {
  override fun start(primaryStage: Stage) {
    val axis = RoundedNumberAxis()
    axis.style = "-fx-background:WHITE;"

    axis.side = Side.LEFT

    val root = BorderPane(axis)
    root.style = "-fx-background:ORANGE;"
    root.padding = Insets(30.0)


    root.bottom = Components.vbox5(
      Components.label(axis.lowerBoundProperty().asString()),
      Components.slider(axis.lowerBoundProperty(), -100.0, 1000.0, 1.0),
      Components.label(axis.upperBoundProperty().asString()),
      Components.slider(axis.upperBoundProperty(), -100.0, 1000.0, 1.0)
    )

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }
}
