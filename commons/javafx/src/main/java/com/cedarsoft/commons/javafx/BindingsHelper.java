package com.cedarsoft.commons.javafx;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class BindingsHelper {
  private BindingsHelper() {
  }

  /**
   * Binds a combo box
   */
  @Nonnull
  public static <T extends Enum<T>> ComboBox<T> bindEnumCombo(@Nonnull ObjectProperty<T> property, @Nonnull Class<T> type) {
    return bindEnumCombo(new ComboBox<>(), property, type);
  }

  @Nonnull
  public static <T extends Enum<T>> ComboBox<T> bindEnumComboSpecificEnums(@Nonnull ObjectProperty<T> property, @Nonnull Class<T> type, @Nonnull T[] enumConstants) {
    return bindEnumComboSpecificEnums(new ComboBox<>(), property, type, enumConstants);
  }

  @Nonnull
  public static <T extends Enum<T>> ComboBox<T> bindEnumCombo(@Nonnull ComboBox<T> combo, @Nonnull ObjectProperty<T> property, @Nonnull Class<T> type) {
    return bindEnumComboSpecificEnums(combo, property, type, type.getEnumConstants());
  }

  @Nonnull
  public static <T extends Enum<T>> ComboBox<T> bindEnumComboSpecificEnums(@Nonnull ComboBox<T> combo, @Nonnull ObjectProperty<T> property, @Nonnull Class<T> type, @Nonnull T[] enumConstants) {
    combo.itemsProperty().set(FXCollections.observableArrayList(enumConstants));
    combo.valueProperty().bindBidirectional(property);

    combo.setButtonCell(new EnumListCell<>());
    combo.setCellFactory(param -> new EnumListCell<>());

    return combo;
  }



  @Nonnull
  public static StringBinding toBinding(@Nonnull StringProperty stringProperty) {
    return Bindings.createStringBinding(stringProperty::get, stringProperty);
  }

  public static <T extends Enum<T>> ComboBox<T> bindEnumCombo(@Nonnull ComboBox<T> combo, @Nonnull ObjectProperty<T> property, @Nonnull ObservableList<T> items) {
    combo.itemsProperty().set(items);
    combo.valueProperty().bindBidirectional(property);

    return combo;
  }

  /**
   * Sets the pseudo class when the given property is true
   */
  public static void bindPseudoClass(@Nonnull Node node, @Nonnull PseudoClass pseudoClass, @Nonnull ObservableBooleanValue pseudoClassActive) {
    //set initially
    node.pseudoClassStateChanged(pseudoClass, pseudoClassActive.get());

    //Update the pseudo class whenever the property changes
    pseudoClassActive.addListener((observable, oldValue, newValue) -> node.pseudoClassStateChanged(pseudoClass, newValue));
  }

  @Nonnull
  public static StringBinding asFormattedHex(@Nonnull ReadOnlyProperty<Number> longProperty) {
    return Bindings.createStringBinding(() -> {
      long longValue = longProperty.getValue().longValue();
      if (longValue == 0) {
        return "";
      }
      return toHex(longValue);
    }, longProperty);
  }

  /**
   * Converts to upper case hex with prefix "0x"
   */
  @Nonnull
  public static String toHex(long longValue) {
    return "0x" + Long.toHexString(longValue).toUpperCase();
  }

  /**
   * Wraps a string binding and ensures that no empty string is returned
   */
  @Nonnull
  public static Binding<String> avoidEmptyString(@Nonnull Binding<String> binding, @Nonnull String emptyStringReplacement) {
    return new Binding<String>() {
      @Override
      public String getValue() {
        String value = binding.getValue();
        if (value == null || value.trim().isEmpty()) {
          return emptyStringReplacement;
        }

        return value;
      }

      @Override
      public boolean isValid() {
        return binding.isValid();
      }

      @Override
      public void invalidate() {
        binding.invalidate();
      }

      @Override
      public ObservableList<?> getDependencies() {
        return binding.getDependencies();
      }

      @Override
      public void dispose() {
        binding.dispose();
      }

      @Override
      public void addListener(ChangeListener<? super String> listener) {
        binding.addListener(listener);
      }

      @Override
      public void removeListener(ChangeListener<? super String> listener) {
        binding.removeListener(listener);
      }

      @Override
      public void addListener(InvalidationListener listener) {
        binding.addListener(listener);
      }

      @Override
      public void removeListener(InvalidationListener listener) {
        binding.removeListener(listener);
      }
    };
  }

  @Nonnull
  public static StringBinding bindFormattedNumber(@Nonnull ObservableValue<Number> property, @Nonnull NumberFormat numberFormat) {
    return Bindings.createStringBinding(() -> numberFormat.format(property.getValue()), property);
  }

  /**
   * Creates a listener that ensures a given property always is within the given min and max value (both inclusive)
   */
  @Nonnull
  public static ChangeListener<Number> createBoundsChecker(double minValue, double maxValue) {
    return (observable, oldValue, newValue) -> {
      if (newValue.doubleValue() < minValue) {
        ((WritableDoubleValue) observable).set(minValue);
      }
      if (newValue.doubleValue() > maxValue) {
        ((WritableDoubleValue) observable).set(maxValue);
      }
    };
  }

  /**
   * Can be used to bind a double property to a string property
   */
  @Nonnull
  public static Property<String> double2stringBiDirectional(@Nonnull DoubleProperty doubleProperty) {
    SimpleStringProperty stringProperty = new SimpleStringProperty();

    stringProperty.addListener((observable, oldValue, newValue) -> {
      try {
        double doubleValue = string2double(newValue);
        doubleProperty.setValue(doubleValue);
      } catch (ParseException e) {
        //Enforce the old values if can not be parsed
        stringProperty.setValue(oldValue);
      }
    });

    doubleProperty.addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        try {
          double oldValueFromString = string2double(stringProperty.get());
          if (oldValueFromString == newValue.doubleValue()) {
            return;
          }
        } catch (ParseException ignore) {
        }

        stringProperty.setValue(NumberFormat.getInstance().format(newValue.doubleValue()));
      }
    });

    return stringProperty;
  }

  /**
   * Converts a string to double. Handles null values and empty strings
   */
  private static double string2double(@Nullable String newValue) throws ParseException {
    if (newValue == null || newValue.isEmpty()) {
      return 0.0;
    }

    return NumberFormat.getInstance().parse(newValue.trim()).doubleValue();
  }

  /**
   * creates a LongBinding from three Integer properties.
   * @param intProperty0
   * @param intProperty1
   * @param intProperty2
   */
  public static LongBinding createLongBinding(@Nonnull ReadOnlyIntegerProperty intProperty0,
                                              @Nonnull ReadOnlyIntegerProperty intProperty1,
                                              @Nonnull ReadOnlyIntegerProperty intProperty2) {

    return Bindings.createLongBinding(() -> {

      Integer byte0 = intProperty0.getValue() & 0xffffff;
      Integer byte1 = intProperty1.getValue() & 0xffff;
      Integer byte2 = intProperty2.getValue() & 0xff;

      return (long) (byte2 + byte1 * 256 + byte0 * 256 * 256);
    }, intProperty0, intProperty1, intProperty2);
  }

  /**
   * Binds two method calls to a boolean property.
   * The method corresponding to the current state is called once initially
   */
  public static void bindMethodsToBoolean(@Nonnull ObservableValue<Boolean> property, @Nonnull Runnable onTrue, @Nonnull Runnable onFalse) {
    property.addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        onTrue.run();
      }
      else {
        onFalse.run();
      }
    });

    //Initial
    if (property.getValue()) {
      onTrue.run();
    }
    else {
      onFalse.run();
    }
  }
}
