package it.neckar.open.swing.binding;

import static org.assertj.core.api.Assertions.*;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.junit.jupiter.api.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

/**
 */
public class JavaBindingTest {
  @Test
  void testCombo() throws Exception {
    JComboBox<String> comboBox = new JComboBox<>();
    BooleanProperty editableProperty = BindingKt.editableProperty(comboBox);
    assertThat(editableProperty.get()).isFalse();

    comboBox.setEditable(true);
    assertThat(editableProperty.get()).isTrue();
  }

  @Test
  void testTextBinding() throws Exception {
    JTextField textField = new JTextField();
    StringProperty textProperty = BindingKt.textProperty(textField);

    assertThat(textProperty.get()).isEqualTo("");
    textField.setText("asdf");
    assertThat(textProperty.get()).isEqualTo("asdf");

    textProperty.setValue("foo");

    assertThat(textProperty.get()).isEqualTo("foo");
    assertThat(textField.getText()).isEqualTo("foo");
  }
}
