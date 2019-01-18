package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.commons.javafx.converter.String2StringConverter;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * Binds a property to a text field that is only updated on focus lost or enter
 */
public class DelayedTextFieldBinding<T> {
  @Nonnull
  private final TextField textField;
  @Nonnull
  private final Property<T> property;
  @Nonnull
  private final StringConverter<T> converter;

  @Nonnull
  private final ChangeListener<T> text2fieldListener;
  @Nonnull
  private final ChangeListener<Boolean> focusLostListener;

  /**
   * Constructor
   *
   * @param textField the target text field
   * @param property  the property the text field's text property is bidirectionally bound to
   * @param converter its {@code toString}-method is called to convert the value of the property to the text of the text field;
   *                  its {@code fromString}-method is called to convert the text of the text field to the value of the property.
   */
  public DelayedTextFieldBinding(@Nonnull TextField textField, @Nonnull Property<T> property, @Nonnull StringConverter<T> converter) {
    this.textField = textField;
    this.property = property;
    this.converter = converter;

    //Set initial value
    updateTextField();

    //Update text field on property change
    text2fieldListener = (observable, oldValue, newValue) -> {
      if (textField.getText().equals(newValue)) {
        return;
      }

      updateTextField();
    };
    property.addListener(text2fieldListener);

    //Update the property on focus lost
    focusLostListener = (observable, oldValue, newValue) -> {
      if (!newValue) {
        updateProperty();
      }
    };
    textField.focusedProperty().addListener(focusLostListener);

    //Update the property on enter
    EventHandler<KeyEvent> enterPressedListener = event -> {
      if (event.getCode() == KeyCode.ENTER) {
        updateProperty();
      }
    };
    textField.onKeyPressedProperty().setValue(enterPressedListener);
  }

  private void updateTextField() {
    textField.setText(converter.toString(property.getValue()));
  }

  /**
   * Writes the value from the text field to the property
   */
  private void updateProperty() {
    property.setValue(converter.fromString(textField.getText()));
  }

  /**
   * Unbinds everything
   */
  public void unbind() {
    property.removeListener(text2fieldListener);
    textField.focusedProperty().removeListener(focusLostListener);
    textField.onKeyPressedProperty().setValue(null);
  }

  @Nonnull
  public TextField getTextField() {
    return textField;
  }

  @Nonnull
  public Property<T> getProperty() {
    return property;
  }

  @Nonnull
  public StringConverter<T> getConverter() {
    return converter;
  }

  @Nonnull
  public static DelayedTextFieldBinding<String> connect(@Nonnull TextField textField, @Nonnull Property<String> text) {
    return new DelayedTextFieldBinding<>(textField, text, new String2StringConverter());
  }

  /**
   * Creates a delayed text field binding.
   *
   * @param textField the target text field
   * @param text      the property the text field's text property is bidirectionally bound to
   * @param converter its {@code toString}-method is called to convert the value of the property to the text of the text field;
   *                  its {@code fromString}-method is called to convert the text of the text field to the value of the property.
   */
  @Nonnull
  public static <T> DelayedTextFieldBinding<T> connect(@Nonnull TextField textField, @Nonnull Property<T> text, @Nonnull StringConverter<T> converter) {
    return new DelayedTextFieldBinding<T>(textField, text, converter);
  }

}
