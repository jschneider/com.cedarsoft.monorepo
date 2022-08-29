package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.Components.vbox5
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.util.Duration

class AdvancedTextFieldDemo : Application() {
  @Throws(Exception::class)
  override fun start(primaryStage: Stage) {
    val property: StringProperty = SimpleStringProperty()

    val advancedTextField = AdvancedTextField()
    advancedTextField.bindTextBidirectional(property)
    advancedTextField.textField.textProperty().addListener(ChangeListener { observable, oldValue, newValue ->
      if (newValue.isEmpty()) {
        return@ChangeListener
      }
      println("Text changed from <$oldValue> to <$newValue>")
      try {
        val parsedValue = newValue.toInt()
        if (parsedValue < 0) {
          advancedTextField.textField.text = "0"
        }
        if (parsedValue > 255) {
          advancedTextField.textField.text = "255"
        }
      } catch (e: NumberFormatException) {
        println("Could not parse")
        advancedTextField.textField.text = oldValue
      }
    })

    val advancedTextField2 = AdvancedTextField()
    advancedTextField2.addCommitHandler { advancedTextField1: AdvancedTextField?, text: String? -> property.value = text }

    val textField = TextField()
    textField.textProperty().bindBidirectional(property)

    val scene = Scene(vbox5(textField, advancedTextField, advancedTextField2), 800.0, 600.0)
    primaryStage.scene = scene
    primaryStage.show()

    val timeline = Timeline(KeyFrame(Duration.millis(500.0), object : EventHandler<ActionEvent?> {
      var counter = 0
      override fun handle(event: ActionEvent?) {
        counter++
        property.set(counter.toString())
      }
    }))
    timeline.cycleCount = Animation.INDEFINITE
    timeline.play()
  }
}
