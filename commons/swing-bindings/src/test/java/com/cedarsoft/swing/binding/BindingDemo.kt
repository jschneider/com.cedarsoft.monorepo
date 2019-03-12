package com.cedarsoft.swing.binding

import javafx.beans.binding.Bindings
import javafx.scene.control.ComboBox
import javafx.scene.control.RadioButton
import net.miginfocom.swing.MigLayout
import org.assertj.core.api.Assertions.*
import java.awt.event.ActionEvent
import java.util.concurrent.Callable
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JRadioButton
import javax.swing.JSlider
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
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

    if (false) {
      val fxComboBox = ComboBox<Member>()
      val itemsProperty = fxComboBox.itemsProperty()
      fxComboBox.valueProperty()
    }

    comboBox.items().add(Member("csdf"))
    comboBox.disableProperty().bindBidirectional(checkboxComboDisabled.selectedProperty())
    comboBox.editableProperty().bindBidirectional(checkboxEditable.selectedProperty())


    labelComboValue.textProperty().bind(comboBox.valueProperty().asString())
    labelComboValueType.textProperty().bind(
      Bindings.createStringBinding(
        Callable<String?> { comboBox.valuePropertyEditable().get()?.javaClass?.name ?: "" },
        comboBox.valueProperty()
      )
    )

    textLabel.textProperty().bind(textField.textProperty())
    textField.textProperty().bindBidirectional(textField2.textProperty())


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

    if (false) {
      val radioButton = RadioButton()
      radioButton.focusedProperty()
    }

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


    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
  }


}

data class Member(val name: String)
