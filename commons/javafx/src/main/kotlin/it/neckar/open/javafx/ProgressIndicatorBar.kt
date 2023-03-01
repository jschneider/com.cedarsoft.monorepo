package it.neckar.open.javafx

import it.neckar.open.javafx.properties.*
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

/**
 * Displays a progress bar with a text indicator inside
 */
class ProgressIndicatorBar(
  val totalWork: Double,
  val labelFormatSpecifier: String,
  val unitLabel: String,
) : StackPane() {

  val workDoneProperty: DoubleProperty = SimpleDoubleProperty()
  var workDone: Double by workDoneProperty

  private val progressBar = ProgressBar()

  private val text = Text()

  init {
    progressBar.maxWidth = Double.MAX_VALUE
    text.fill = Color.BLACK
    text.font = Font.font(text.font.family, FontWeight.BOLD, 14.0)
    syncProgress()
    workDoneProperty.addListener { observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? -> syncProgress() }
    children.setAll(progressBar, text)
  }

  // synchronizes the progress indicated with the work done.
  private fun syncProgress() {
    if (totalWork < 0) {
      text.text = ""
      progressBar.progress = ProgressIndicator.INDETERMINATE_PROGRESS
    } else {
      text.text = String.format(labelFormatSpecifier, workDone) + " " + unitLabel
      progressBar.progress = workDone / totalWork
    }
    progressBar.minHeight = text.boundsInLocal.height + DEFAULT_LABEL_PADDING * 2
    progressBar.minWidth = text.boundsInLocal.width + DEFAULT_LABEL_PADDING * 2
  }

  companion object {
    private const val DEFAULT_LABEL_PADDING = 2
  }
}
