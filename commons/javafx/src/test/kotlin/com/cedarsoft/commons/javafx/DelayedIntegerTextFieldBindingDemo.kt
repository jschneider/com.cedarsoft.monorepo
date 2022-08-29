package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.Components.label
import com.cedarsoft.commons.javafx.Components.textFieldIntegerDelayed
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class DelayedIntegerTextFieldBindingDemo : Application() {
  override fun start(primaryStage: Stage) {
    val integerProperty: IntegerProperty = SimpleIntegerProperty(8000)

    val vBox = VBox(10.0)
    vBox.padding = Insets(20.0)
    vBox.children.add(label(Bindings.createStringBinding({ Integer.toString(integerProperty.get()) }, integerProperty)))

    val randomButton = Button("Random!")
    randomButton.onAction = EventHandler { evt: ActionEvent? -> integerProperty.set((8000 * Math.random()).toInt()) }
    vBox.children.add(randomButton)

    val hBox1 = HBox(10.0)
    hBox1.children.add(label("Delayed"))
    hBox1.children.add(textFieldIntegerDelayed(integerProperty))
    vBox.children.add(hBox1)

    val hBox2 = HBox(10.0)
    hBox2.children.add(label("Delayed, only positive"))
    hBox2.children.add(textFieldIntegerDelayed(integerProperty, { i: Int -> i >= 0 }))
    vBox.children.add(hBox2)

    val hBox3 = HBox(10.0)
    hBox3.children.add(label("Delayed, only even"))
    hBox3.children.add(textFieldIntegerDelayed(integerProperty, { i: Int -> i % 2 == 0 }))
    vBox.children.add(hBox3)

    val hBox4 = HBox(40.0)
    hBox4.children.add(label("Focus catcher"))
    hBox4.children.add(TextField())
    vBox.children.add(hBox4)

    primaryStage.scene = Scene(vBox)
    primaryStage.show()
  }
}
