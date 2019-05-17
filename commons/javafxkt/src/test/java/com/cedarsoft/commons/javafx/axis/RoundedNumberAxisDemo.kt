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
    val axisLeft = RoundedNumberAxis()
    axisLeft.style = "-fx-background:WHITE;"
    axisLeft.side = Side.LEFT

    val axisRight = RoundedNumberAxis()
    axisRight.style = "-fx-background:WHITE;"
    axisRight.side = Side.RIGHT

    val axisTop = RoundedNumberAxis()
    axisTop.style = "-fx-background:WHITE;"
    axisTop.side = Side.TOP

    val axisBottom = RoundedNumberAxis()
    axisBottom.style = "-fx-background:WHITE;"
    axisBottom.side = Side.BOTTOM



    axisRight.lowerBoundProperty().bind(axisLeft.lowerBoundProperty())
    axisRight.upperBoundProperty().bind(axisLeft.upperBoundProperty())
    axisTop.lowerBoundProperty().bind(axisLeft.lowerBoundProperty())
    axisTop.upperBoundProperty().bind(axisLeft.upperBoundProperty())
    axisBottom.lowerBoundProperty().bind(axisLeft.lowerBoundProperty())
    axisBottom.upperBoundProperty().bind(axisLeft.upperBoundProperty())


    val axisPane = BorderPane()
    BorderPane.setMargin(axisTop, Insets(5.0, 5.0, 5.0, 5.0))
    BorderPane.setMargin(axisLeft, Insets(5.0, 5.0, 5.0, 5.0))
    BorderPane.setMargin(axisRight, Insets(5.0, 5.0, 5.0, 5.0))
    BorderPane.setMargin(axisBottom, Insets(5.0, 5.0, 5.0, 5.0))

    axisPane.left = axisLeft
    axisPane.right = axisRight
    axisPane.top = axisTop
    axisPane.bottom = axisBottom

    val root = BorderPane(axisPane)

    root.style = "-fx-background:ORANGE;"
    root.padding = Insets(30.0)

    root.bottom = Components.vbox5(
      Components.label(axisLeft.lowerBoundProperty().asString()),
      Components.slider(axisLeft.lowerBoundProperty(), -100.0, 1000.0, 1.0),
      Components.label(axisLeft.upperBoundProperty().asString()),
      Components.slider(axisLeft.upperBoundProperty(), -100.0, 1000.0, 1.0)
    )

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }
}
