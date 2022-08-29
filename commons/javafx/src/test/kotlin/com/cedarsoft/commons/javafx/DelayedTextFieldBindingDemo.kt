package com.cedarsoft.commons.javafx

import com.cedarsoft.commons.javafx.Components.hbox5
import com.cedarsoft.commons.javafx.Components.label
import com.cedarsoft.commons.javafx.Components.textField
import com.cedarsoft.commons.javafx.Components.textFieldDelayed
import com.cedarsoft.commons.javafx.Components.vbox5
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
