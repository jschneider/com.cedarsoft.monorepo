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
import javax.swing.JTextField

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

    val comboBox = JComboBox<Member>(ComboBoxObservableListModel(Member("asdf"), Member("bsdf")))
    val labelComboValue = JLabel()
    val labelComboValueType = JLabel()

    if (false) {
      val fxComboBox = ComboBox<Member>()
      val itemsProperty = fxComboBox.itemsProperty()
      fxComboBox.valueProperty()
    }

    Binding.getItems(comboBox).add(Member("csdf"))
    Binding.getDisableProperty(comboBox).bindBidirectional(Binding.getSelectedProperty(checkboxComboDisabled))
    Binding.getEditableProperty(comboBox).bindBidirectional(Binding.getSelectedProperty(checkboxEditable))


    Binding.getTextProperty(labelComboValue).bind(Binding.getValueProperty(comboBox).asString())
    Binding.getTextProperty(labelComboValueType).bind(
      Bindings.createStringBinding(
        Callable<String?> { Binding.getValuePropertyEditable(comboBox).get()?.javaClass?.name ?: "" },
        Binding.getValueProperty(comboBox)
      )
    )

    Binding.getTextProperty(textLabel).bind(Binding.getTextProperty(textField))
    Binding.getTextProperty(textField).bindBidirectional(Binding.getTextProperty(textField2))


    Binding.getSelectedProperty(checkboxSelected).bindBidirectional(Binding.getSelectedProperty(checkboxSelected2))
    Binding.getDisableProperty(textField).bindBidirectional(Binding.getSelectedProperty(checkboxDisabled))


    assertThat(Binding.getTextProperty(textLabel)).isSameAs(Binding.getTextProperty(textLabel))
    assertThat(Binding.getSelectedProperty(checkboxSelected)).isSameAs(Binding.getSelectedProperty(checkboxSelected))
    assertThat(Binding.getSelectedProperty(checkboxSelected2)).isSameAs(Binding.getSelectedProperty(checkboxSelected2))

    Binding.getTextProperty(textLabelFocused).bind(Binding.getFocusedProperty(textField).asString())

    Binding.getSelectedProperty(radio1).bind(Binding.getTextProperty(textField).isEqualTo("val"))
    Binding.getSelectedProperty(radio2).bind(Binding.getTextProperty(textField).isNotEqualTo("val"))

    Binding.getDisableProperty(radio1).value = true
    Binding.getDisableProperty(radio2).value = true

    Binding.getTextProperty(checkboxSelected).bind(Binding.getTextProperty(textField))
    Binding.getTextProperty(radio1).bind(Binding.getTextProperty(textField))

    //Progress bar
    Binding.getMaximumProperty(progressBar).value = 100
    Binding.getMinimumProperty(progressBar).value = 2
    Binding.getValueProperty(progressBar).value = 50
    Binding.getIndeterminateProperty(progressBar).bind(Binding.getSelectedProperty(checkboxSelected))

    //Slider
    Binding.getMaximumProperty(slider).value = 100
    Binding.getMinimumProperty(slider).value = 2

    Binding.getValueProperty(progressBar).bind(Binding.getValueProperty(slider))
    Binding.getValueProperty(slider).value = 50

    if (false) {
      val radioButton = RadioButton()
      radioButton.focusedProperty()
    }

    //    Binding.bindText(textFieldUni, textProperty)
    //    Binding.bindText(textProperty, textLabel)
    //    Binding.bindTextBidirectional(textProperty, textFieldBi)
    //
    //    Binding.bindDisabled(disabledProperty, textFieldBi)
    //
    //    Binding.bindSelectedBidirectional(disabledProperty, checkboxSelected)


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
    contentPane.add(comboBox)
    contentPane.add(labelComboValue)
    contentPane.add(labelComboValueType)

    contentPane.add(JButton(object : AbstractAction("Add element") {
      override fun actionPerformed(e: ActionEvent?) {
        val items = Binding.getItems(comboBox)
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
