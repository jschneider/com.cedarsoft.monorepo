package com.cedarsoft.commons.javafx.combo

import com.cedarsoft.commons.javafx.Components
import com.cedarsoft.commons.javafx.Components.button
import javafx.application.Application
import javafx.beans.Observable
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.util.Callback
import java.util.function.Consumer

fun main() {
  Application.launch(ComboBoxWithOptionalDemo::class.java)
}

class ComboBoxWithOptionalDemo() : Application() {
  private val options = CrossBeamConfig.values().map {
    ItemThatMayBeDisabled(it)
  }.also {
    //Disable two options
    it[1].disabled = true
    it[3].disabled = true
  }

  private val observableOptions: ObservableList<ItemThatMayBeDisabled<CrossBeamConfig>> = FXCollections.observableArrayList(Callback<ItemThatMayBeDisabled<CrossBeamConfig>, Array<Observable>> { param -> arrayOf(param.disabledProperty) }).also {
    it.addAll(options)
  }

  private val property: ObjectProperty<CrossBeamConfig> = SimpleObjectProperty(CrossBeamConfig.Crossbeam7x)

  override fun start(primaryStage: Stage) {
    val root = BorderPane()

    property.addListener { _, oldValue, newValue -> println("Property changed from $oldValue to $newValue") }

    val comboBox: ComboBox<ItemThatMayBeDisabled<CrossBeamConfig>> = Components.comboBoxWithOptionalItems(property, observableOptions) {
      it.name
    }

    root.center = comboBox
    root.bottom = button("toggle") {
      options.forEach(Consumer { item: ItemThatMayBeDisabled<CrossBeamConfig> -> item.disabled = !item.disabled })
    }

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()

  }
}

enum class CrossBeamConfig {
  Disabled,
  Crossbeam3x,
  Crossbeam5x,
  Crossbeam7x,
  Crossbeam9x,
}
