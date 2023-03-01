package it.neckar.open.javafx

import it.neckar.open.javafx.Components.hbox5
import it.neckar.open.javafx.Components.label
import it.neckar.open.javafx.Components.textField
import it.neckar.open.javafx.Components.textFieldDelayed
import it.neckar.open.javafx.Components.vbox5
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.Scene
import javafx.stage.Stage

class DelayedTextFieldBindingDemo : Application() {
  override fun start(primaryStage: Stage) {
    val property: StringProperty = SimpleStringProperty("da content")
    primaryStage.scene = Scene(
      vbox5(
        label(property),
        hbox5(label("Normal"), textField(property)),
        hbox5(label("Delayed 1"), textFieldDelayed(property)),
        hbox5(label("Delayed 2"), textFieldDelayed(property))
      ), 800.0, 600.0
    )
    primaryStage.show()
  }
}
