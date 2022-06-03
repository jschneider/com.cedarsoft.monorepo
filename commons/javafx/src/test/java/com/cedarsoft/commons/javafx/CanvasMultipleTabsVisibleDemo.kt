package com.cedarsoft.commons.javafx

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

/**
 */
fun main() {
  launch(CanvasMultipleTabsVisibleDemo::class.java)
}

class CanvasMultipleTabsVisibleDemo : Application() {
  override fun start(primaryStage: Stage) {

    val root = BorderPane()

    val tabPane = TabPane()
    root.center = tabPane

    tabPane.tabs.add(Tab("Tab 1", MyNode("1")))
    tabPane.tabs.add(Tab("Tab 2", MyNode("2")))

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }
}

class MyNode(val label: String) : Pane() {
  init {
    children.add(Label("Hello $label"))

    println("initial visibility for $label: <$isVisible> for parent $parent")
    println("initial focus isFocusTraversable for $label: <$isFocusTraversable>")

    parentProperty().addListener { _, oldValue, newValue ->
      println("parent changed for $label from $oldValue to $newValue")
    }

    visibleProperty().addListener { _, oldValue, newValue ->
      println("Visibility changed for $label from <$oldValue> to <$newValue>")
    }
    focusTraversableProperty().addListener { _, oldValue, newValue ->
      println("focus traversable changed for $label from <$oldValue> to <$newValue>")
    }

  }
}
