package com.cedarsoft.test.tornadofx.view

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.events
import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.toBinding
import com.github.thomasnield.rxkotlinfx.toMaybe
import com.github.thomasnield.rxkotlinfx.toObservable
import com.github.thomasnield.rxkotlinfx.toObservableChanges
import io.reactivex.rxjavafx.observables.JavaFxObservable
import io.reactivex.schedulers.Schedulers
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.util.Duration
import java.util.logging.Logger

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main(args: Array<String>) {
  launch(JavaFxRxAppSample::class.java)
}


class JavaFxRxAppSample : Application() {
  override fun start(primaryStage: Stage) {
    val button = Button("Hello World")
    bind(button)

    val textField = TextField()
    bind(textField)

    val spinner = Spinner<Int>()
    spinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100)
    spinner.isEditable = true
    val spinnerChangesLabel = Label()

    bind(spinner, spinnerChangesLabel)


    val root = BorderPane()
    root.bottom = button
    root.center = VBox(spinner, spinnerChangesLabel)
    root.top = textField

    primaryStage.scene = Scene(root, 800.0, 600.0)
    primaryStage.show()
  }

  private fun bind(spinner: Spinner<Int>, spinnerChangesLabel: Label) {
    //JavaFxObservable
    //  .changesOf(spinner.valueProperty())
    //  .map(change -> "OLD: " + change.getOldVal() + " NEW: " + change.getNewVal())
    //  .subscribe(spinnerChangesLabel::setText);

    val spinnerChangesAsString = spinner.valueProperty()
      .toObservableChanges()
      .map { change ->
        "OLD: " + change.oldVal + " NEW: " + change.newVal
      }.toBinding()

    spinnerChangesLabel.textProperty().bind(
      spinnerChangesAsString
    )
  }

  private fun bind(textField: TextField) {
    textField.textProperty().toObservable()
      .subscribe {
        LOG.info("Text field text changed to: $it")
      }
  }

  private fun bind(button: Button) {
    button.events(ActionEvent.ACTION)
      .subscribe {
        LOG.info("on next $it")
      }

    button.actionEvents()
      .subscribe { actionEvent ->
        LOG.info("Action event " + actionEvent.source)

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Confirmation"
        alert.headerText = "Please confirm your action"
        alert.contentText = "Are you ok with this?"

        alert.toMaybe()
          .filter { response ->
            response == ButtonType.OK
          }.subscribe { buttonType ->
            LOG.info("Alert closed with ok$buttonType")
          }
      }

    JavaFxObservable.interval(Duration.millis(1000.0))
      .observeOn(Schedulers.computation())
      .map { aLong ->
        Thread.sleep(150)
        aLong.toString() + " with waiting"
      }
      .observeOnFx()
      .subscribe { button.text = it }
  }

  companion object {
    private val LOG = Logger.getLogger(JavaFxRxAppSample::class.java.name)
  }
}
