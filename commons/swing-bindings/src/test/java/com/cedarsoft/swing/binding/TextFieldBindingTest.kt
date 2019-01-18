package com.cedarsoft.swing.binding

import javafx.beans.property.SimpleStringProperty
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.swing.JTextField

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class TextFieldBindingTest {
  @Test
  internal fun testTextFieldText() {
    val property = SimpleStringProperty()
    assertThat(property.get()).isNull()

    val textField = JTextField()
    textField.text = "asdf"

    Binding.bindText(textField, property)
    assertThat(property.get()).isEqualTo("asdf")

    textField.text = "newText"
    assertThat(property.get()).isEqualTo("newText")
  }
}
