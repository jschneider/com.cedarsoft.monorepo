package it.neckar.open.swing.binding

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JTextField
import javax.swing.SwingUtilities

object SwingFxBridgeDemo {
  private fun initAndShowGUI() {
    // This method is invoked on the EDT thread
    val frame = JFrame("Swing and JavaFX")
    val fxPanel = JFXPanel()
    frame.contentPane.layout = BorderLayout()
    frame.contentPane.add(fxPanel, BorderLayout.CENTER)

    frame.contentPane.add(JTextField("asdf"), BorderLayout.SOUTH)

    frame.setSize(300, 200)
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    Platform.runLater { initFX(fxPanel) }
  }

  private fun initFX(fxPanel: JFXPanel) {
    // This method is invoked on the JavaFX thread
    val scene = createScene()
    fxPanel.scene = scene
  }

  private fun createScene(): Scene {
    val root = BorderPane()
    val scene = Scene(root, Color.ALICEBLUE)

    root.center = TextField("asdfasdf")
    root.top = Label("asdfasdf")

    return scene
  }

  @JvmStatic
  fun main(args: Array<String>) {
    SwingUtilities.invokeLater { initAndShowGUI() }
  }
}
