package com.cedarsoft.commons.javafx

import com.cedarsoft.formatting.decimalFormat
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Scene
import javafx.scene.control.Slider
import javafx.stage.Stage
import org.tbee.javafx.scene.layout.MigPane

/**
 *
 */
fun main() {
  Application.launch(SliderDemo::class.java)
}

class SliderDemo : Application() {
  override fun start(primaryStage: Stage) {
    val root = MigPane()

    val valueProperty = SimpleDoubleProperty(17.0)
    valueProperty.consume {
      println("Value changed to $it")
    }

    val slider = Slider(0.0, 100.0, 5.0)
    Bindings.bindBidirectional(valueProperty, slider.valueProperty())

    root.add(Components.label("Slider:"))
    root.add(slider, "wrap")
    root.add(Components.label("Value:"))
    root.add(Components.label(valueProperty.map { decimalFormat.format(it.toDouble()) }), "wrap")

    root.add(Components.label("Min:"))
    root.add(Components.label(slider.minProperty().map { decimalFormat.format(it.toDouble()) }), "wrap")
    root.add(Components.label("Max:"))
    root.add(Components.label(slider.maxProperty().map { decimalFormat.format(it.toDouble()) }), "wrap")

    root.add(Components.button("Set prop to 10.0") {
      valueProperty.value = 10.0
    }, "")
    root.add(Components.button("Set prop to 90.0") {
      valueProperty.value = 90.0
    }, "wrap")

    root.add(Components.button("Set max to 50.0") {
      slider.max = 50.0
    }, "")
    root.add(Components.button("Set max to 100.0") {
      slider.max = 100.0
    }, "wrap")

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }
}
