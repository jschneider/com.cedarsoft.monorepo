package com.cedarsoft.commons.javafx;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.unit.other.px;
import com.google.common.collect.ImmutableList;
import com.sun.javafx.binding.BidirectionalBinding;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

/**
 * Helper stuff for the components
 */
public class Components {
  private static String ENUM_VALUE = "enumValue";

  private Components() {
  }

  /**
   * Creates a new hbox with a spacing of 15 px
   */
  @Nonnull
  public static HBox hbox15(@Nonnull Node... nodes) {
    return hbox(15, nodes);
  }

  @Nonnull
  public static HBox hbox5(@Nonnull Node... nodes) {
    return hbox5(ImmutableList.copyOf(nodes));
  }

  @Nonnull
  public static HBox hbox5(@Nonnull Collection<? extends Node> nodes) {
    return hbox(5, nodes);
  }

  @Nonnull
  public static HBox hbox(@px int spacing, @Nonnull Node... nodes) {
    return hbox(spacing, ImmutableList.copyOf(nodes));
  }

  @Nonnull
  public static HBox hbox(@px int spacing, @Nonnull Collection<? extends Node> nodes) {
    HBox hBox = new HBox();
    hBox.getChildren().addAll(nodes);
    hBox.setSpacing(spacing);
    hBox.setAlignment(Pos.CENTER_LEFT);
    return hBox;
  }

  /**
   * Creates a new vBox with a spacing of 15 px
   */
  @Nonnull
  public static VBox vbox15(@Nonnull Node... nodes) {
    return vbox(15, nodes);
  }

  @Nonnull
  public static VBox vbox5(@Nonnull Node... nodes) {
    return vbox(5, nodes);
  }

  @Nonnull
  public static VBox vbox(@px int spacing, @Nonnull Node... nodes) {
    return vbox(spacing, Pos.CENTER_LEFT, nodes);
  }

  @Nonnull
  public static VBox vbox(@px int spacing, @Nonnull Pos alignment, @Nonnull Node... nodes) {
    VBox vBox = new VBox(nodes);
    vBox.setSpacing(spacing);
    vBox.setAlignment(alignment);
    return vBox;
  }

  @Nonnull
  public static <T extends Region> T padding(@Nonnull T node, @Nonnull Insets insets) {
    node.setPadding(insets);
    return node;
  }

  /**
   * Creates a new label and binds the given string binding to the text property
   */
  @Nonnull
  public static Label label(@Nonnull ObservableValue<String> binding) {
    Label label = new Label();
    label.textProperty().bind(binding);
    return label;
  }

  /**
   * Calls toString for the value returned by the binding
   */
  @Nonnull
  public static Label labelToString(@Nonnull ObservableValue<?> binding) {
    Label label = new Label();
    label.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(binding.getValue()), binding));
    return label;
  }

  @Nonnull
  public static Label labelInteger(@Nonnull IntegerProperty integerProperty) {
    Label label = new Label();
    minWidth2pref(label);
    NumberStringConverterForIntegers converter = new NumberStringConverterForIntegers();
    integerProperty.addListener((observable, oldValue, newValue) -> label.setText(converter.toString(newValue)));
    label.setText(converter.toString(integerProperty.get()));
    return label;
  }

  /**
   * Sets the min width to use the pref size
   */
  @Nonnull
  public static <T extends Region> T minWidth2pref(@Nonnull T node) {
    node.setMinWidth(Region.USE_PREF_SIZE);
    return node;
  }

  /**
   * Creates a label with the min width set to USE_PREF
   */
  @Nonnull
  public static Label minWidth2prefLabel(@Nonnull String text) {
    Label label = new Label(text);
    return minWidth2pref(label);
  }

  @Nonnull
  public static Label label() {
    return label("");
  }

  /**
   * Creates a label with min width set to pref
   */
  @Nonnull
  public static Label label(@Nonnull String text) {
    return minWidth2prefLabel(text);
  }

  @Nonnull
  public static Label headline1(@Nonnull String headline) {
    Label label = new Label(headline);
    label.getStyleClass().add("headline-1");
    return label;
  }

  @Nonnull
  public static Label headline2(@Nonnull String headline) {
    Label label = new Label(headline);
    label.getStyleClass().add("headline-2");
    return label;
  }

  @Nonnull
  public static CheckBox checkBox(@Nonnull String text, @Nonnull BooleanProperty selected) {
    CheckBox checkBox = new CheckBox(text);
    checkBox.selectedProperty().bindBidirectional(selected);
    return checkBox;
  }

  @Nonnull
  public static <T> HBox createRadioButtons(@Nonnull Property<T> property, @Nonnull Collection<? extends T> values) {
    return createRadioButtons(null, property, values.stream());
  }

  @SafeVarargs
  @Nonnull
  public static <T> HBox createRadioButtons(@Nonnull Property<T> property, @Nonnull T... values) {
    return createRadioButtons(null, property, Arrays.stream(values));
  }

  @SafeVarargs
  @Nonnull
  public static <T> HBox createRadioButtons(@Nullable String label, @Nonnull Property<T> property, @Nonnull T... values) {
    return createRadioButtons(label, property, Arrays.stream(values));
  }

  @Nonnull
  public static <T> HBox createRadioButtons(@Nullable String label, @Nonnull Property<T> property, @Nonnull Stream<? extends T> values) {

    ToggleGroup toggleGroup = new ToggleGroup();

    List<RadioButton> buttons = values
                                  .map(value -> {
                                    RadioButton radioButton = new RadioButton(value.toString());
                                    radioButton.setToggleGroup(toggleGroup);
                                    radioButton.getProperties().put(ENUM_VALUE, value);
                                    return radioButton;
                                  })
                                  .collect(Collectors.toList());


    toggleGroup.selectedToggleProperty().addListener((obs, ov, newValue) -> {
      property.setValue((T) newValue.getProperties().get(ENUM_VALUE));
    });

    property.addListener((obs, ov, nv) -> {
      if (nv != null) {
        selectButton(toggleGroup, nv);
      }
    });
    @Nullable T initialValue = property.getValue();
    if (initialValue != null) {
      selectButton(toggleGroup, initialValue);
    }


    HBox hBox = new HBox();
    if (label != null) {
      hBox.getChildren().add(new Label(label));
    }
    hBox.getChildren().addAll(buttons);
    return hBox;
  }

  private static <T> void selectButton(@Nonnull ToggleGroup toggleGroup, @Nonnull T value) {
    toggleGroup.getToggles()
      .stream()
      .filter(radioButton -> radioButton.getProperties().get(ENUM_VALUE).equals(value))
      .forEach(toggleGroup::selectToggle);
  }

  /**
   * Creates radio buttons for enumerations.
   * These buttons are added to a toggle group
   */
  @Nonnull
  public static <E extends Enum<E>> List<? extends RadioButton> radioButtons(@Nonnull ObjectProperty<E> enumProperty, @Nonnull E[] values) {
    ToggleGroup toggleGroup = new ToggleGroup();

    return Arrays.stream(values)
             .map(value -> radioButton(enumProperty, value))
             .peek(radioButton -> radioButton.setToggleGroup(toggleGroup))
             .collect(Collectors.toList());
  }

  /**
   * Creates a radio button whose selection property is bidirectionally bound to the given enum property.
   */
  @Nonnull
  public static <E extends Enum<E>> RadioButton radioButton(@Nonnull ObjectProperty<E> enumProperty, @Nonnull E value) {
    RadioButton radioButton = new RadioButton(EnumTranslatorUtil.getEnumTranslator().translate(value));
    radioButton.setMinWidth(Region.USE_PREF_SIZE);
    enumProperty.addListener((observable, oldValue, newValue) -> radioButton.setSelected(newValue == value));
    radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        enumProperty.set(value);
      }
    });
    //set initially
    radioButton.setSelected(value == enumProperty.getValue());
    return radioButton;
  }

  @Nonnull
  public static RadioButton[] radioButtons(@Nonnull BooleanProperty booleanProperty, @Nonnull String textTrue, @Nonnull String textFalse) {
    ToggleGroup toggleGroup = new ToggleGroup();

    RadioButton radioButtonTrue = new RadioButton(textTrue);
    radioButtonTrue.setToggleGroup(toggleGroup);
    radioButtonTrue.selectedProperty().bindBidirectional(booleanProperty);

    RadioButton radioButtonFalse = new RadioButton(textFalse);
    radioButtonFalse.setToggleGroup(toggleGroup);

    booleanProperty.addListener((observable, oldValue, newValue) -> {
      radioButtonTrue.selectedProperty().set(newValue);
      radioButtonFalse.selectedProperty().set(!newValue);
    });

    if (booleanProperty.get()) {
      radioButtonTrue.setSelected(true);
    }
    else {
      radioButtonFalse.setSelected(true);
    }

    return new RadioButton[]{radioButtonTrue, radioButtonFalse};
  }

  @Nonnull
  public static Node createCheckBox(@Nonnull String label, @Nonnull BooleanProperty booleanProperty) {
    CheckBox checkBox = new CheckBox(label);
    checkBox.selectedProperty().bindBidirectional(booleanProperty);
    return checkBox;
  }

  @Nonnull
  public static Button button(@Nonnull String text, @Nonnull EventHandler<ActionEvent> onAction) {
    Button button = new Button(text);
    button.setOnAction(onAction);
    return button;
  }

  @Nonnull
  public static TextField textField(@Nonnull StringProperty text) {
    TextField textField = new TextField();
    textField.textProperty().bindBidirectional(text);
    return textField;
  }

  @Nonnull
  public static <T> TextField textField(@Nonnull Property<T> property, @Nonnull StringConverter<T> converter) {
    TextField textField = new TextField();
    textField.textProperty().bindBidirectional(property, converter);
    return textField;
  }

  /**
   * Binds the text field to the property. Updates are only written to the property on focus lost or enter
   */
  @Nonnull
  public static TextField textFieldDelayed(@Nonnull StringProperty text) {
    TextField textField = new TextField();
    DelayedTextFieldBinding.connect(textField, text);
    return textField;
  }

  /**
   * Creates a text field with a {@link DelayedTextFieldBinding}.
   *
   * @param text      the property the text field's text property is bidirectionally bound to
   * @param converter its {@code toString}-method is called to convert the value of the property to the text of the text field;
   *                  its {@code fromString}-method is called to convert the text of the text field to the value of the property.
   */
  @Nonnull
  public static <T> TextField textFieldDelayed(@Nonnull Property<T> text, @Nonnull StringConverter<T> converter) {
    TextField textField = new TextField();
    DelayedTextFieldBinding.connect(textField, text, converter);
    return textField;
  }

  @Nonnull
  public static <E extends Enum<E>> TextField textFieldEnumReadOnly(@Nonnull ObjectProperty<E> enumProperty) {
    TextField textField = new TextField();
    textField.setEditable(false);
    textField.textProperty().bind(Bindings.createStringBinding(() -> EnumTranslatorUtil.getEnumTranslator().translate(enumProperty.get()), enumProperty));
    return textField;
  }

  @Nonnull
  public static TextField textFieldReadonly(@Nonnull ObservableValue<? extends String> text) {
    TextField textField = new TextField();
    textField.textProperty().bind(text);
    textField.setEditable(false);
    return textField;
  }

  @Nonnull
  public static TextField textFieldIntegerReadonly(@Nonnull ReadOnlyIntegerProperty integerProperty) {
    return textFieldIntegerReadonly(integerProperty, new NumberStringConverterForIntegers());
  }

  @Nonnull
  public static TextField textFieldIntegerReadonly(@Nonnull ReadOnlyIntegerProperty integerProperty, @Nonnull NumberStringConverterForIntegers integerConverter) {
    TextField textField = new TextField();
    textField.alignmentProperty().set(Pos.TOP_RIGHT);
    integerProperty.addListener((observable, oldValue, newValue) -> textField.setText(integerConverter.toString(newValue)));
    textField.setText(integerConverter.toString(integerProperty.get()));
    textField.setEditable(false);
    return textField;
  }

  @Nonnull
  public static TextField textFieldFloatReadonly(@Nonnull ReadOnlyFloatProperty floatProperty) {
    return textFieldFloatReadonly(floatProperty, new NumberStringConverterForFloatingPointNumbers());
  }

  @Nonnull
  public static TextField textFieldFloatReadonly(@Nonnull ReadOnlyFloatProperty floatProperty, @Nonnull NumberStringConverterForFloatingPointNumbers floatConverter) {
    TextField textField = new TextField();
    textField.alignmentProperty().set(Pos.TOP_RIGHT);
    floatProperty.addListener((observable, oldValue, newValue) -> textField.setText(floatConverter.toString(newValue)));
    textField.setText(floatConverter.toString(floatProperty.get()));
    textField.setEditable(false);
    return textField;
  }


  @Nonnull
  public static TextField textFieldDoubleReadonly(@Nonnull ReadOnlyDoubleProperty doubleProperty) {
    return textFieldDoubleReadonly(doubleProperty, new NumberStringConverterForFloatingPointNumbers());
  }

  @Nonnull
  public static TextField textFieldDoubleReadonly(@Nonnull ReadOnlyDoubleProperty doubleProperty, @Nonnull NumberStringConverterForFloatingPointNumbers floatConverter) {
    TextField textField = new TextField();
    textField.alignmentProperty().set(Pos.TOP_RIGHT);
    doubleProperty.addListener((observable, oldValue, newValue) -> textField.setText(floatConverter.toString(newValue)));
    textField.setText(floatConverter.toString(doubleProperty.get()));
    textField.setEditable(false);
    return textField;
  }

  @Nonnull
  public static TextField textFieldDoubleDelayed(@Nonnull DoubleProperty doubleProperty) {
    return textFieldDoubleDelayed(doubleProperty, d -> true);
  }

  @Nonnull
  public static TextField textFieldDoubleDelayed(@Nonnull DoubleProperty doubleProperty, @Nonnull NumberStringConverterForFloatingPointNumbers converter) {
    return textFieldDoubleDelayed(doubleProperty, d -> true, converter);
  }

  @Nonnull
  public static TextField textFieldDoubleDelayed(@Nonnull DoubleProperty doubleProperty, @Nonnull Predicate<Double> filter) {
    return textFieldDoubleDelayed(doubleProperty, filter, new NumberStringConverterForFloatingPointNumbers());
  }

  @Nonnull
  public static TextField textFieldDoubleDelayed(@Nonnull DoubleProperty doubleProperty, @Nonnull Predicate<Double> filter, @Nonnull NumberStringConverterForFloatingPointNumbers converter) {
    TextField textField = textFieldDelayed(doubleProperty, converter);
    textField.alignmentProperty().set(Pos.CENTER_RIGHT);
    applyDoubleFormatter(textField, doubleProperty, filter, converter);
    return textField;
  }

  /**
   * Adds a text formatter to the text field that only allows adding doubles
   */
  public static void applyDoubleFormatter(@Nonnull TextField textField, @Nonnull DoubleProperty doubleProperty, @Nonnull Predicate<Double> filter, @Nonnull NumberStringConverterForFloatingPointNumbers converter) {
    textField.setTextFormatter(new TextFormatter<>(converter, doubleProperty.get(), new DoubleTextFormatterFilter(converter, filter)));
  }

  @Nonnull
  public static TextField textFieldIntegerDelayed(@Nonnull IntegerProperty integerProperty) {
    return textFieldIntegerDelayed(integerProperty, i -> true);
  }

  @Nonnull
  public static TextField textFieldIntegerDelayed(@Nonnull IntegerProperty integerProperty, @Nonnull Predicate<Integer> filter) {
    NumberStringConverterForIntegers converter = new NumberStringConverterForIntegers();
    TextField textField = textFieldDelayed(integerProperty, converter);
    textField.alignmentProperty().set(Pos.CENTER_RIGHT);
    applyIntegerFormatter(textField, integerProperty, filter, converter);
    return textField;
  }

  /**
   * Adds a text formatter to the text field that only allows adding integers
   */
  public static void applyIntegerFormatter(@Nonnull TextField textField, @Nonnull IntegerProperty integerProperty, @Nonnull Predicate<Integer> filter, @Nonnull NumberStringConverterForIntegers converter) {
    textField.setTextFormatter(new TextFormatter<>(converter, integerProperty.get(), new IntegerTextFormatterFilter(converter, filter)));
  }

  @Nonnull
  public static Spinner<Integer> spinner(@Nonnull IntegerProperty valueProperty) {
    return spinner(valueProperty, 0);
  }

  @Nonnull
  public static Spinner<Integer> spinner(@Nonnull IntegerProperty valueProperty, final int defaultValue) {
    return spinner(valueProperty, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Creates a Spinner instance with the {@link Spinner#valueFactoryProperty() value factory} set to be an instance
   * of {@link SpinnerValueFactory.IntegerSpinnerValueFactory}.
   *
   * @param valueProperty the property the value property of the spinner factory is bound to.
   * @param minValue      The minimum allowed integer value for the Spinner.
   * @param maxValue      The maximum allowed integer value for the Spinner.
   * @param initialValue  The value of the Spinner when first instantiated.
   */
  @Nonnull
  public static Spinner<Integer> spinner(@Nonnull IntegerProperty valueProperty, final int initialValue, final int minValue, final int maxValue) {
    Spinner<Integer> spinner = new Spinner<>(minValue, maxValue, initialValue);
    SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();
    spinner.setOnScroll(event -> {
      if (spinner.isDisabled()) {
        return;
      }
      if (!spinner.isVisible()) {
        return;
      }
      double delta = event.getDeltaY();
      if (delta == 0.0) {
        return;
      }
      int increment = delta < 0 ? -1 : +1;
      valueFactory.setValue(spinner.getValue() + increment);
    });
    BidirectionalBinding.bindNumber(valueProperty, valueFactory.valueProperty());
    return spinner;
  }

  @Nonnull
  public static Label placeholder(@Nonnull String text) {
    Label placeholder = new Label(text);
    placeholder.setFont(new Font(30.0));
    placeholder.setTextFill(Color.gray(0.4));
    placeholder.setTextAlignment(TextAlignment.CENTER);
    placeholder.setAlignment(Pos.CENTER);
    placeholder.setStyle("-fx-background-color: #f8f8f8");
    return placeholder;
  }

  /**
   * Creates a checkbox that supports three states: All selected / none selected / some selected
   */
  @Nonnull
  public static CheckBox checkboxTriState(@Nonnull String label, @Nonnull TriStateCheckBox.Callback callback, @Nonnull ObservableObjectValue<TriStateCheckboxState> state) {
    TriStateCheckBox triStateCheckBox = new TriStateCheckBox(label, callback, state);
    return triStateCheckBox.getCheckBox();
  }

  @Nonnull
  public static CheckBox checkboxTriState(@Nonnull String label, @Nonnull TriStateCheckBox.Callback callback, @Nonnull ReadOnlyBooleanProperty allSelectedProperty, @Nonnull ReadOnlyBooleanProperty noneSelectedProperty) {
    return checkboxTriState(label, callback, TriStateCheckBox.createStateProperty(allSelectedProperty, noneSelectedProperty));
  }

  @Nonnull
  public static Slider slider(@Nonnull IntegerProperty integerProperty, int minValue, int maxValue, int step) {
    Slider slider = new Slider();
    slider.setMin(minValue);
    slider.setMax(maxValue);
    slider.setMajorTickUnit(step);
    slider.setSnapToTicks(true); // skip double values
    com.cedarsoft.commons.javafx.BidirectionalBinding.bindBidirectional(integerProperty,
                                                                        slider.valueProperty(),
                                                                        (observable, oldValue, newValue) -> {
                                                                          // property -> slider
                                                                          slider.valueProperty().set(getNextStepForValue(newValue.intValue(), (int) slider.getMin(), (int) slider.getMax(), step));
                                                                        },
                                                                        (observable, oldValue, newValue) -> {
                                                                          // slider -> property
                                                                          integerProperty.set(getNextStepForValue(newValue.intValue(), (int) slider.getMin(), (int) slider.getMax(), step));
                                                                        });
    return slider;
  }

  private static int getNextStepForValue(int value, final int min, final int max, final int step) {
    int nextStep = (value / step) * step;
    if (nextStep < min) {
      nextStep = min;
    }
    if (nextStep > max) {
      nextStep = max;
    }
    return nextStep;
  }

}
