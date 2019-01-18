package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.si.ms;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * A text field that is bound to a value but is not updated while it has the focus
 */
public final class AdvancedTextField extends StackPane {
  @Nonnull
  private final TextField textField;

  public AdvancedTextField() {
    textField = new TextField();
    getChildren().add(textField);
  }

  /**
   * A handler that is executed on enter or focus lost
   */
  public void addCommitHandler(@Nonnull CommitHandler commitHandler) {
    textField.focusedProperty().addListener((observable, oldValue, newValue) -> commitHandler.commit(AdvancedTextField.this, textField.getText()));
    textField.setOnAction(event -> commitHandler.commit(AdvancedTextField.this, textField.getText()));
  }

  public void bindTextBidirectional(@Nonnull StringProperty property) {
    bindTextBidirectional(property, new DefaultStringConverter());
  }

  /**
   * The timestamp of the last relevant interaction (input / focus change)
   */
  @ms
  private long lastChange = -1;

  private void updateLastChange(@Nonnull String reason) {
    lastChange = System.currentTimeMillis();
  }

  public <T> void bindTextBidirectional(@Nonnull Property<T> property, @Nonnull StringConverter<T> converter) {
    //Commit the text on enter
    textField.setOnAction(event -> {
      property.setValue(converter.fromString(textField.getText()));
      lastChange = -1; //reset the last change to ensure updates are reflected
    });

    //Fill the text field initially
    textField.setText(converter.toString(property.getValue()));

    //Remember the last time a key has been pressed
    textField.caretPositionProperty().addListener((observable, oldValue, newValue) -> updateLastChange("caret position changed"));
    textField.onKeyPressedProperty().addListener((observable, oldValue, newValue) -> updateLastChange("key pressed"));
    textField.focusedProperty().addListener((observable, oldValue, newValue) -> updateLastChange("focus changed"));
    textField.selectedTextProperty().addListener((observable, oldValue, newValue) -> updateLastChange("selected text changed"));

    property.addListener((observable, oldValue, newValue) -> {
      if (textField.isFocused()) {
        //Field is focused
        @ms long timeSinceLastInput = System.currentTimeMillis() - lastChange;
        if (timeSinceLastInput < 3000) {
          //Only overwrite if the last edit action is at least 1 sec old
          return;
        }
      }

      textField.setText(converter.toString(newValue));
    });

    //Update the property when the text field changes
    textField.textProperty().addListener((observable, oldValue, newValue) -> property.setValue(converter.fromString(newValue)));

    //Update the text field on focus lost
    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        textField.setText(converter.toString(property.getValue()));
      }
    });
  }

  @Nonnull
  public BooleanProperty editableProperty() {
    return textField.editableProperty();
  }

  public void setPrefColumnCount(int value) {
    textField.setPrefColumnCount(value);
  }

  @Nonnull
  public TextField getTextField() {
    return textField;
  }

  /**
   * Handler that is called when the text field is commited
   */
  @FunctionalInterface
  public interface CommitHandler {
    void commit(@Nonnull AdvancedTextField advancedTextField, @Nonnull String text);
  }
}
