package com.cedarsoft.commons.javafx.style

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.stage.Stage
import org.tbee.javafx.scene.layout.MigPane

/**
 */
fun main(args: Array<String>) {
  launch(StyleDemo::class.java, *args)
}

class StyleDemo : Application() {
  override fun start(primaryStage: Stage) {
    val root = MigPane("insets 20")
    root.add(Label("A label"))
    root.add(Button("Hello World"))

    primaryStage.scene = Scene(root)
    primaryStage.scene.stylesheets.add(javaClass.getResource("nice-style.css").toExternalForm())

    primaryStage.show()
  }
}
