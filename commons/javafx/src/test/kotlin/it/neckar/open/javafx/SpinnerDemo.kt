package it.neckar.open.javafx

import it.neckar.open.formatting.decimalFormat
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.Scene
import javafx.scene.control.Slider
import javafx.stage.Stage
import org.tbee.javafx.scene.layout.MigPane

/**
 *
 */
fun main() {
  Application.launch(SpinnerDemo::class.java)
}

class SpinnerDemo : Application() {
  override fun start(primaryStage: Stage) {
    val root = MigPane()

    val valueProperty = SimpleIntegerProperty(17)
    valueProperty.consume {
      println("Value changed to $it")
    }

    val slider = Components.spinner(valueProperty)

    root.add(Components.label("Spinner:"))
    root.add(slider, "wrap")
    root.add(Components.label("Value:"))
    root.add(Components.label(valueProperty.map { decimalFormat.format(it.toDouble()) }), "wrap")

    //root.add(Components.label("Min:"))
    //root.add(Components.label(slider.minProperty().map { decimalFormat.format(it.toDouble()) }), "wrap")
    //root.add(Components.label("Max:"))
    //root.add(Components.label(slider.maxProperty().map { decimalFormat.format(it.toDouble()) }), "wrap")

    root.add(Components.button("Set prop to 10.0") {
      valueProperty.value = 10
    }, "")
    root.add(Components.button("Set prop to 90.0") {
      valueProperty.value = 90
    }, "wrap")

    //root.add(Components.button("Set max to 50.0") {
    //  slider.max = 50.0
    //}, "")
    //root.add(Components.button("Set max to 100.0") {
    //  slider.max = 100.0
    //}, "wrap")

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }
}
