package com.cedarsoft.swing.binding

import javafx.beans.property.SimpleStringProperty
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.swing.JComboBox
import javax.swing.JTextField

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class BindingTest {
  @Test
  internal fun testCombo() {
    val comboBox = JComboBox<String>(ComboBoxObservableListModel())
    assertThat(comboBox.items()).hasSize(0)
  }

  @Test
  internal fun testTextField() {
    val textField = JTextField()
    textField.textProperty()
  }

  @Test
  internal fun testTextFieldText() {
    val property = SimpleStringProperty()
    assertThat(property.get()).isNull()

    val textField = JTextField()
    textField.text = "asdf"

    bindText(textField, property)
    assertThat(property.get()).isEqualTo("asdf")

    textField.text = "newText"
    assertThat(property.get()).isEqualTo("newText")
  }

  @Test
  internal fun testVisible() {
    val textField = JTextField()
    val visibleProperty = textField.visibleProperty()

    assertThat(visibleProperty.get()).isTrue()
    assertThat(textField.isVisible).isTrue()

    visibleProperty.set(false)
    assertThat(visibleProperty.get()).isFalse()
    assertThat(textField.isVisible).isFalse()

    visibleProperty.set(true)
    assertThat(visibleProperty.get()).isTrue()
    assertThat(visibleProperty.get()).isTrue()
    assertThat(textField.isVisible).isTrue()
  }
}
