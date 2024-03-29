package it.neckar.open.swing.binding

import assertk.*
import assertk.assertions.*
import javafx.beans.binding.Bindings
import net.miginfocom.swing.MigLayout
import org.junit.jupiter.api.Disabled
import java.awt.Color
import java.awt.event.ActionEvent
import java.util.concurrent.Callable
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JRadioButton
import javax.swing.JSlider
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.SwingWorker

/**
 */
@Disabled
object BindingDemo {
  @JvmStatic
  fun main(args: Array<String>) {
    val frame = JFrame()

    val textLabel = JLabel("Label Text")
    val textLabelFocused = JLabel("Label Focused")
    val textField = JTextField("")
    val textField2 = JTextField("")
    val checkboxSelected = JCheckBox("selected")
    val checkboxSelected2 = JCheckBox("selected2")

    val checkboxDisabled = JCheckBox("disabled")
    val checkboxComboDisabled = JCheckBox("disabled")
    val checkboxEditable = JCheckBox("editable")
    val checkboxVisible = JCheckBox("visible")

    val radio1 = JRadioButton("radio 1")
    val radio2 = JRadioButton("radio 2")

    val progressBar = JProgressBar()
    val slider = JSlider()

    val spinner = JSpinner(SpinnerNumberModel(1.0, 0.0, 100.0, 1.0))
    val spinnerStepSizeLabel = JLabel("...")
    val spinnerValueLabel = JLabel("...")

    val comboBox = JComboBox<Member>(ComboBoxObservableListModel(Member("asdf"), Member("bsdf")))
    val labelComboValue = JLabel()
    val labelComboValueType = JLabel()

    comboBox.items().add(Member("csdf"))
    comboBox.disableProperty().bindBidirectional(checkboxComboDisabled.selectedProperty())
    comboBox.editableProperty().bindBidirectional(checkboxEditable.selectedProperty())
    comboBox.visibleProperty().bindBidirectional(checkboxVisible.selectedProperty())


    labelComboValue.textProperty().bind(comboBox.valueProperty().asString())
    labelComboValueType.textProperty().bind(
      Bindings.createStringBinding(
        Callable<String?> { comboBox.valuePropertyEditable().get()?.javaClass?.name.orEmpty() },
        comboBox.valueProperty()
      )
    )

    textLabel.textProperty().bind(textField.textProperty())
    textField.textProperty().bindBidirectional(textField2.textProperty())
    textLabel.foregroundProperty().set(Color.ORANGE)
    textLabel.backgroundProperty().set(Color.BLUE)
    textLabel.isOpaque = true

    checkboxSelected.selectedProperty().bindBidirectional(checkboxSelected2.selectedProperty())
    textField.disableProperty().bindBidirectional(checkboxDisabled.selectedProperty())


    assertThat(textLabel.textProperty()).isSameAs(textLabel.textProperty())
    assertThat(checkboxSelected.selectedProperty()).isSameAs(checkboxSelected.selectedProperty())
    assertThat(checkboxSelected2.selectedProperty()).isSameAs(checkboxSelected2.selectedProperty())

    textLabelFocused.textProperty().bind(textField.focusedProperty().asString())

    radio1.selectedProperty().bind(textField.textProperty().isEqualTo("val"))
    radio2.selectedProperty().bind(textField.textProperty().isNotEqualTo("val"))

    radio1.disableProperty().value = true
    radio2.disableProperty().value = true

    checkboxSelected.textProperty().bind(textField.textProperty())
    radio1.textProperty().bind(textField.textProperty())

    //Progress bar
    progressBar.maximumProperty().value = 100
    progressBar.minimumProperty().value = 2
    progressBar.valueProperty().value = 50
    progressBar.indeterminateProperty().bind(checkboxSelected.selectedProperty())

    //Slider
    slider.maximumProperty().value = 100
    slider.minimumProperty().value = 2

    progressBar.valueProperty().bind(slider.valueProperty())
    slider.valueProperty().value = 50

    //Spinner
    spinner.minimumProperty().value = -100.0
    spinner.maximumProperty().value = 100.0
    spinner.valueProperty().bindBidirectional(slider.valueProperty())
    spinnerStepSizeLabel.textProperty().bind(spinner.stepSizeProperty().asString())
    spinnerValueLabel.textProperty().bind(spinner.valueProperty().asString())

    //    bindText(textFieldUni, textProperty)
    //    bindText(textProperty, textLabel)
    //    bindTextBidirectional(textProperty, textFieldBi)
    //
    //    bindDisabled(disabledProperty, textFieldBi)
    //
    //    bindSelectedBidirectional(disabledProperty, checkboxSelected)


    val contentPane = frame.contentPane
    contentPane.layout = MigLayout("", "[fill][fill, grow]")


    contentPane.add(JLabel("JLabel:"), "")
    contentPane.add(textLabel, "wrap")

    contentPane.add(JLabel("Text Field:"), "")
    contentPane.add(textField, "wrap")

    contentPane.add(JLabel("Text Field 2:"), "")
    contentPane.add(textField2, "wrap")

    contentPane.add(JLabel("Text Field Focused"), "")
    contentPane.add(textLabelFocused, "wrap")

    contentPane.add(JLabel("Disabled"), "")
    contentPane.add(checkboxDisabled, "wrap")

    contentPane.add(checkboxSelected, "span, split 2")
    contentPane.add(checkboxSelected2, "span, split 2")

    contentPane.add(radio1, "span, split 2")
    contentPane.add(radio2, "span, split 2")

    contentPane.add(progressBar, "span")
    contentPane.add(slider, "span")
    contentPane.add(spinner, "span")
    contentPane.add(JLabel("Step Size:"), "")
    contentPane.add(spinnerStepSizeLabel, "")
    contentPane.add(JLabel("Spinner Value:"), "")
    contentPane.add(spinnerValueLabel, "span")
    contentPane.add(comboBox)
    contentPane.add(labelComboValue)
    contentPane.add(labelComboValueType)

    contentPane.add(JButton(object : AbstractAction("Add element") {
      override fun actionPerformed(e: ActionEvent?) {
        val items = comboBox.items()
        items.add(Member("newElement: " + items.size))
      }
    }), "wrap")

    contentPane.add(checkboxComboDisabled, "")
    contentPane.add(checkboxEditable, "wrap")
    contentPane.add(checkboxVisible, "wrap")

    val button = JButton(object : AbstractAction("Disable on click") {
      override fun actionPerformed(e: ActionEvent) {
        (e.source as JComponent).isEnabled = false
        object : SwingWorker<Unit, Unit>() {
          override fun doInBackground() {
            Thread.sleep(1000)
            return
          }

          override fun done() {
            super.done()
            (e.source as JComponent).isEnabled = true
          }
        }.execute()

      }
    })
    button.disableProperty().bind(checkboxDisabled.selectedProperty())
    contentPane.add(button, "wrap")


    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
  }


}

data class Member(val name: String)
