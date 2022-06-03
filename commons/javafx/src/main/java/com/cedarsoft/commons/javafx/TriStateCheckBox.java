package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

/**
 * Helper class that supports a tri state checkbox
 *
 */
public class TriStateCheckBox {
  @Nonnull
  private final CheckBox checkBox;
  @Nonnull
  private final Callback callback;
  @Nonnull
  private final ObservableObjectValue<TriStateCheckboxState> state;


  public TriStateCheckBox(@Nonnull String label, @Nonnull Callback callback, @Nonnull ObservableObjectValue<TriStateCheckboxState> state) {
    checkBox = new CheckBox(label);
    //Set selected to true initially to allow an initial indeterminate state
    checkBox.setSelected(true);

    this.callback = callback;
    this.state = state;

    //Update the model when the checkbox has changed
    checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
      /**
       * Is set to true during modification to avoid duplicate events
       */
      private boolean isChanging;

      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (isChanging) {
          return;
        }

        if (newValue) {
          isChanging = true;
          try {
            TriStateCheckBox.this.callback.selectAll();
          }
          finally {
            isChanging = false;
          }
        }
        else {
          try {
            isChanging = true;
            TriStateCheckBox.this.callback.selectNone();
          }
          finally {
            isChanging = false;
          }
        }
      }
    });

    //Update the checkbox if the state has changed
    this.stateProperty().addListener((observable, oldValue, newValue) -> updateSelectAllCheckbox(checkBox));
    updateSelectAllCheckbox(checkBox);
  }

  /**
   * Creates a binding to a tri state checkbox state
   */
  @Nonnull
  public static ObjectBinding<TriStateCheckboxState> createStateProperty(@Nonnull ReadOnlyBooleanProperty allSelectedProperty, @Nonnull ReadOnlyBooleanProperty noneSelectedProperty) {
    return Bindings.createObjectBinding(() -> {
      if (allSelectedProperty.get()) {
        return TriStateCheckboxState.SELECTED;
      }
      if (noneSelectedProperty.get()) {
        return TriStateCheckboxState.UNSELECTED;
      }

      return TriStateCheckboxState.INDETERMINATE;
    }, allSelectedProperty, noneSelectedProperty);
  }

  private void updateSelectAllCheckbox(@Nonnull CheckBox selectAllCheckBox) {
    switch (stateProperty().get()) {
      case UNSELECTED:
        selectAllCheckBox.setIndeterminate(false);
        selectAllCheckBox.setSelected(false);
        return;
      case INDETERMINATE:
        selectAllCheckBox.setIndeterminate(true);
        return;
      case SELECTED:
        selectAllCheckBox.setIndeterminate(false);
        selectAllCheckBox.setSelected(true);
        return;
    }

    throw new IllegalArgumentException("Invalid state <" + stateProperty().get() + ">");
  }

  @Nonnull
  public CheckBox getCheckBox() {
    return checkBox;
  }

  @Nonnull
  public ObservableObjectValue<TriStateCheckboxState> stateProperty() {
    return state;
  }

  /**
   * A model for the tri state checkbox
   */
  public interface Callback {

    /**
     * Select all elements
     */
    void selectAll();

    /**
     * Select no elements
     */
    void selectNone();
  }

}
