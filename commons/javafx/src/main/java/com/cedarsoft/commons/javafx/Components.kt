package com.cedarsoft.commons.javafx

import com.cedarsoft.unit.other.px
import com.google.common.collect.ImmutableList
import com.sun.javafx.binding.BidirectionalBinding
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerExpression
import javafx.beans.binding.LongExpression
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyFloatProperty
import javafx.beans.property.ReadOnlyLongProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter
import java.util.Arrays
import java.util.concurrent.Callable
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.streams.toList

/**
 * Helper stuff for the components
 */
object Components {
  private const val ENUM_VALUE: String = "enumValue"

  @JvmStatic
  fun hbox(@px spacing: Int, vararg nodes: Node): HBox {
    return hbox(spacing, ImmutableList.copyOf(nodes))
  }

  @JvmStatic
  fun hbox(@px spacing: Int, nodes: Collection<Node>): HBox {
    val hBox = HBox()
    hBox.children.addAll(nodes)
    hBox.spacing = spacing.toDouble()
    hBox.alignment = Pos.CENTER_LEFT
    return hBox
  }

  @JvmStatic
  fun hbox5(nodes: Collection<Node>): HBox {
    return hbox(5, nodes)
  }

  /**
   * Creates a new hbox with a spacing of 5 of the given nodes
   */
  @JvmStatic
  fun hbox5(vararg nodes: Node): HBox {
    return hbox5(ImmutableList.copyOf(nodes))
  }

  @JvmStatic
  fun hbox5(labelText: String, vararg nodes: Node): HBox {
    return hbox5(label(labelText), listOf(*nodes))
  }

  @JvmStatic
  fun hbox5(node: Node, nodes: Collection<Node>): HBox {
    return hbox5(
      ImmutableList.builder<Node>()
        .add(node)
        .addAll(nodes)
        .build()
    )
  }

  /**
   * Creates a new hbox with a spacing of 15 px
   */
  @JvmStatic
  fun hbox15(vararg nodes: Node): HBox {
    return hbox(15, *nodes)
  }

  /**
   * Creates a new vBox with a spacing of 15 px
   */
  @JvmStatic
  fun vbox15(vararg nodes: Node): VBox {
    return vbox(15, *nodes)
  }

  @JvmStatic
  fun vbox5(vararg nodes: Node): VBox {
    return vbox(5, *nodes)
  }

  @JvmStatic
  fun vbox(@px spacing: Int, vararg nodes: Node): VBox {
    return vbox(spacing, Pos.CENTER_LEFT, *nodes)
  }

  @JvmStatic
  fun vbox(@px spacing: Int, alignment: Pos, vararg nodes: Node): VBox {
    val vBox = VBox(*nodes)
    vBox.spacing = spacing.toDouble()
    vBox.alignment = alignment
    return vBox
  }

  /**
   * Sets the padding of the given region to the insets and returns the region
   */
  @JvmStatic
  fun <T : Region> padding(region: T, insets: Insets): T {
    region.padding = insets
    return region
  }

  /**
   * Creates a new label and binds the given string binding to the text property
   */
  @JvmStatic
  fun label(binding: ObservableValue<out @JvmWildcard String>): Label {
    val label = Label()
    label.textProperty().bind(binding)
    return label
  }

  /**
   * Calls toString for the value returned by the binding
   */
  @JvmStatic
  fun labelToString(binding: ObservableValue<*>): Label {
    val label = Label()
    label.textProperty().bind(Bindings.createStringBinding(Callable { binding.value.toString() }, binding))
    return label
  }

  /**
   * Calls the given [converter] to convert values from [binding] to string
   */
  @JvmStatic
  fun <T> labelToString(binding: ObservableValue<T>, converter: (T) -> String): Label {
    val label = Label()
    label.textProperty().bind(Bindings.createStringBinding(Callable { converter.invoke(binding.value) }, binding))
    return label
  }

  @JvmStatic
  fun labelInteger(integerProperty: IntegerExpression): Label {
    return labelNumber(integerProperty, NumberStringConverterForIntegers())
  }

  @JvmStatic
  fun labelLong(longProperty: LongExpression): Label {
    return labelNumber(longProperty, NumberStringConverterForIntegers())
  }

  @JvmStatic
  fun labelNumber(numberProperty: ObservableValue<Number>, numberStringConverter: StringConverter<Number>): Label {
    val label = Label()
    minWidth2pref(label)
    numberProperty.addListener { _, _, newValue -> label.text = numberStringConverter.toString(newValue) }
    label.text = numberStringConverter.toString(numberProperty.value)
    return label
  }


  /**
   * Sets the min width to use the pref size
   */
  @JvmStatic
  fun <T : Region> minWidth2pref(node: T): T {
    node.minWidth = Region.USE_PREF_SIZE
    return node
  }

  /**
   * Creates a label with the min width set to USE_PREF
   */
  @JvmStatic
  fun minWidth2prefLabel(text: String): Label {
    val label = Label(text)
    return minWidth2pref(label)
  }

  /**
   * Creates a label with min width set to pref
   */
  @JvmStatic
  @JvmOverloads
  fun label(text: String = ""): Label {
    return minWidth2prefLabel(text)
  }

  /**
   * Creates a label with min width set to pref and a ToolTip
   */
  @JvmStatic
  fun labelWithToolTip(text: String, toolTipText: String): Label {
    val label = minWidth2prefLabel(text)
    label.tooltip = Tooltip(toolTipText)
    return label
  }

  /**
   * Creates a label with the min width set to USE_PREF, styled with styleClass
   */
  @JvmStatic
  fun styledLabel(text: String, styleClass: String): Label {
    val label = Label(text)
    label.styleClass.add(styleClass)
    return minWidth2pref(label)
  }

  /**
   * Creates a new styled label and binds the given string binding to the text property
   */
  @JvmStatic
  fun styledLabel(binding: ObservableValue<String>, styleClass: String): Label {
    val label = Label()
    label.styleClass.add(styleClass)
    label.textProperty().bind(binding)
    return label
  }

  @JvmStatic
  fun headline1(headline: String): Label {
    val label = Label(headline)
    label.styleClass.add("headline-1")
    return label
  }

  @JvmStatic
  fun headline2(headline: String): Label {
    val label = Label(headline)
    label.styleClass.add("headline-2")
    return label
  }

  @JvmStatic
  fun checkBox(text: String, selected: Property<Boolean>): CheckBox {
    val checkBox = CheckBox(text)
    checkBox.selectedProperty().bindBidirectional(selected)
    return checkBox
  }

  @JvmStatic
  fun <T> createRadioButtons(property: Property<T>, values: Collection<T>): HBox {
    return createRadioButtons<T>(null, property, values.stream())
  }

  @JvmStatic
  @SafeVarargs
  fun <T> createRadioButtons(property: Property<T>, vararg values: T): HBox {
    return createRadioButtons<T>(null, property, Arrays.stream<T>(values))
  }

  @JvmStatic
  @SafeVarargs
  fun <T> createRadioButtons(label: String?, property: Property<T>, vararg values: T): HBox {
    return createRadioButtons(label, property, Arrays.stream<T>(values))
  }

  @JvmStatic
  fun <T> createRadioButtons(label: String?, property: Property<T>, values: Stream<out T>): HBox {
    val toggleGroup = ToggleGroup()

    val buttons = values
      .map { value ->
        val radioButton = RadioButton(value.toString())
        radioButton.toggleGroup = toggleGroup
        radioButton.properties[ENUM_VALUE] = value
        radioButton
      }
      .toList()


    toggleGroup.selectedToggleProperty().addListener { _, _, newValue -> property.setValue(newValue.properties[ENUM_VALUE] as T) }

    property.addListener { _, _, nv ->
      if (nv != null) {
        selectButton<T>(toggleGroup, nv)
      }
    }
    val initialValue = property.value
    if (initialValue != null) {
      selectButton<T>(toggleGroup, initialValue)
    }


    val hBox = HBox()
    if (label != null) {
      hBox.children.add(Label(label))
    }
    hBox.children.addAll(buttons)
    return hBox
  }

  @JvmStatic
  private fun <T> selectButton(toggleGroup: ToggleGroup, value: T) {
    toggleGroup.toggles
      .stream()
      .filter { radioButton -> radioButton.properties[ENUM_VALUE] == value }
      .forEach(Consumer<Toggle> { toggleGroup.selectToggle(it) })
  }

  /**
   * Creates radio buttons for enumerations.
   * These buttons are added to a toggle group
   */
  @JvmStatic
  fun <E : Enum<E>> radioButtons(enumProperty: Property<E>, values: Array<E>): List<RadioButton> {
    val toggleGroup = ToggleGroup()

    return Arrays.stream(values)
      .map { value -> radioButton(enumProperty, value) }
      .peek { radioButton -> radioButton.toggleGroup = toggleGroup }
      .toList()
  }

  /**
   * Creates a radio button whose selection property is bidirectionally bound to the given enum property.
   */
  @JvmStatic
  fun <E : Enum<E>> radioButton(enumProperty: Property<E>, value: E): RadioButton {
    val radioButton = RadioButton(EnumTranslatorUtil.getEnumTranslator().translate(value))
    radioButton.minWidth = Region.USE_PREF_SIZE
    enumProperty.addListener { _, _, newValue -> radioButton.isSelected = newValue === value }
    radioButton.selectedProperty().addListener { _, _, newValue ->
      if (newValue) {
        enumProperty.value = value
      }
    }
    //set initially
    radioButton.isSelected = value === enumProperty.value
    return radioButton
  }

  @JvmStatic
  fun radioButtons(booleanProperty: Property<Boolean>, textTrue: String, textFalse: String): Array<RadioButton> {
    val toggleGroup = ToggleGroup()

    val radioButtonTrue = RadioButton(textTrue)
    radioButtonTrue.toggleGroup = toggleGroup
    radioButtonTrue.selectedProperty().bindBidirectional(booleanProperty)

    val radioButtonFalse = RadioButton(textFalse)
    radioButtonFalse.toggleGroup = toggleGroup

    booleanProperty.addListener { _, _, newValue ->
      radioButtonTrue.selectedProperty().set(newValue!!)
      radioButtonFalse.selectedProperty().set(!newValue)
    }

    if (booleanProperty.value) {
      radioButtonTrue.isSelected = true
    } else {
      radioButtonFalse.isSelected = true
    }

    return arrayOf(radioButtonTrue, radioButtonFalse)
  }

  @JvmStatic
  fun <E : Enum<E>> comboBox(enumProperty: Property<E>, values: Array<E>): ComboBox<E> {
    val comboBox = ComboBox(FXCollections.observableArrayList(*values))
    comboBox.valueProperty().bindBidirectional(enumProperty)
    EnumListCell.createFor(comboBox)
    return comboBox
  }

  @JvmStatic
  fun <T> comboBox(enumProperty: Property<T>, values: List<T>, converter: (T) -> String): ComboBox<T> {
    val comboBox = ComboBox(FXCollections.observableArrayList(values))
    comboBox.valueProperty().bindBidirectional(enumProperty)
    ConverterListCell.createFor(comboBox, converter)
    return comboBox
  }

  @JvmStatic
  fun createCheckBoxReadOnly(label: String, selectedProperty: Property<Boolean>): Node {
    val checkBox = CheckBox(label)
    checkBox.selectedProperty().bind(selectedProperty)
    checkBox.addEventFilter(EventType.ROOT) { it.consume() }
    return checkBox
  }

  @JvmStatic
  fun createCheckBox(label: String, selectedProperty: Property<Boolean>): Node {
    val checkBox = CheckBox(label)
    checkBox.selectedProperty().bindBidirectional(selectedProperty)
    return checkBox
  }

  /**
   * Legacy method that could be used from Java
   */
  @JvmStatic
  fun buttonWithEventHandler(text: String, onAction: EventHandler<ActionEvent>): Button {
    val button = Button(text)
    button.onAction = onAction
    return button
  }

  @JvmStatic
  fun button(text: String, onAction: (ActionEvent) -> Unit): Button {
    val button = Button(text)
    button.onAction = EventHandler {
      onAction(it)
    }
    return button
  }

  @JvmStatic
  fun textField(text: Property<String>): TextField {
    val textField = TextField()
    textField.textProperty().bindBidirectional(text)
    return textField
  }

  @JvmStatic
  fun <T> textField(property: Property<T>, converter: StringConverter<T>): TextField {
    val textField = TextField()
    textField.textProperty().bindBidirectional(property, converter)
    return textField
  }

  /**
   * Binds the text field to the property. Updates are only written to the property on focus lost or enter
   */
  @JvmStatic
  fun textFieldDelayed(text: Property<String>): TextField {
    val textField = TextField()
    DelayedTextFieldBinding.connect(textField, text)
    return textField
  }

  /**
   * Creates a text field with a [DelayedTextFieldBinding].
   *
   * @param text      the property the text field's text property is bidirectionally bound to
   * @param converter its `toString`-method is called to convert the value of the property to the text of the text field;
   * its `fromString`-method is called to convert the text of the text field to the value of the property.
   */
  @JvmStatic
  fun <T> textFieldDelayed(text: Property<T>, converter: StringConverter<T>): TextField {
    val textField = TextField()
    DelayedTextFieldBinding.connect(textField, text, converter)
    return textField
  }

  @JvmStatic
  fun <E : Enum<E>> textFieldEnumReadOnly(enumProperty: Property<E>): TextField {
    val textField = TextField()
    textField.isEditable = false
    textField.textProperty().bind(Bindings.createStringBinding(Callable { EnumTranslatorUtil.getEnumTranslator().translate(enumProperty.value) }, enumProperty))
    return textField
  }

  @JvmStatic
  fun textFieldReadonly(text: String): TextField {
    val textField = TextField()
    textField.text = text
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  fun textFieldReadonly(text: ObservableValue<out String>): TextField {
    val textField = TextField()
    textField.textProperty().bind(text)
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldIntegerReadonly(integerProperty: ObservableValue<Number>, numberStringConverter: NumberStringConverter = NumberStringConverterForIntegers()): TextField {
    val textField = TextField()
    textField.alignmentProperty().set(Pos.TOP_RIGHT)
    integerProperty.addListener { _, _, newValue -> textField.text = numberStringConverter.toString(newValue) }
    textField.text = numberStringConverter.toString(integerProperty.value)
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldLongReadonly(longProperty: ReadOnlyLongProperty, integerConverter: NumberStringConverter = NumberStringConverterForIntegers()): TextField {
    val textField = TextField()
    textField.alignmentProperty().set(Pos.TOP_RIGHT)
    longProperty.addListener { _, _, newValue -> textField.text = integerConverter.toString(newValue) }
    textField.text = integerConverter.toString(longProperty.get())
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldFloatReadonly(floatProperty: ReadOnlyFloatProperty, floatConverter: NumberStringConverter = NumberStringConverterForFloatingPointNumbers()): TextField {
    val textField = TextField()
    textField.alignmentProperty().set(Pos.TOP_RIGHT)
    floatProperty.addListener { _, _, newValue -> textField.text = floatConverter.toString(newValue) }
    textField.text = floatConverter.toString(floatProperty.get())
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  fun textFieldFloatDelayed(floatProperty: FloatProperty, converter: NumberStringConverter): TextField {
    return textFieldFloatDelayed(floatProperty, Predicate { _ -> true }, converter)
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldFloatDelayed(floatProperty: FloatProperty, filter: Predicate<Float> = Predicate { _ -> true }, converter: NumberStringConverter = NumberStringConverterForFloatingPointNumbers()): TextField {
    val textField = textFieldDelayed(floatProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyFloatFormatter(textField, floatProperty, filter, converter)
    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding floats
   */
  @JvmStatic
  fun applyFloatFormatter(textField: TextField, floatProperty: FloatProperty, filter: Predicate<Float>, converter: NumberStringConverter) {
    textField.textFormatter = TextFormatter(converter, floatProperty.get(), FloatTextFormatterFilter(converter, filter))
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldDoubleReadonly(doubleProperty: ReadOnlyDoubleProperty, floatConverter: NumberStringConverterForFloatingPointNumbers = NumberStringConverterForFloatingPointNumbers()): TextField {
    val textField = TextField()
    textField.alignmentProperty().set(Pos.TOP_RIGHT)
    doubleProperty.addListener { _, _, newValue -> textField.text = floatConverter.toString(newValue) }
    textField.text = floatConverter.toString(doubleProperty.get())
    textField.isEditable = false
    return textField
  }

  @JvmStatic
  fun textFieldDoubleDelayed(doubleProperty: DoubleProperty, converter: NumberStringConverterForFloatingPointNumbers): TextField {
    return textFieldDoubleDelayed(doubleProperty, Predicate { _ -> true }, converter)
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldDoubleDelayed(doubleProperty: DoubleProperty, filter: Predicate<Double> = Predicate { _ -> true }, converter: NumberStringConverterForFloatingPointNumbers = NumberStringConverterForFloatingPointNumbers()): TextField {
    val textField = textFieldDelayed(doubleProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyDoubleFormatter(textField, doubleProperty, filter, converter)
    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding doubles
   */
  @JvmStatic
  fun applyDoubleFormatter(textField: TextField, doubleProperty: DoubleProperty, filter: Predicate<Double>, converter: NumberStringConverterForFloatingPointNumbers) {
    textField.textFormatter = TextFormatter(converter, doubleProperty.get(), DoubleTextFormatterFilter(converter, filter))
  }

  @JvmStatic
  fun textFieldIntegerDelayed(integerProperty: IntegerProperty, converter: NumberStringConverter): TextField {
    return textFieldIntegerDelayed(integerProperty, Predicate { _ -> true }, converter)
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldIntegerDelayed(integerProperty: IntegerProperty, filter: Predicate<Int> = Predicate { _ -> true }, converter: NumberStringConverter = NumberStringConverterForIntegers()): TextField {
    val textField = textFieldDelayed(integerProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyIntegerFormatter(textField, integerProperty, filter, converter)
    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding integers
   */
  @JvmStatic
  fun applyIntegerFormatter(textField: TextField, integerProperty: IntegerProperty, filter: Predicate<Int>, converter: NumberStringConverter) {
    textField.textFormatter = TextFormatter(converter, integerProperty.get(), IntegerTextFormatterFilter(converter, filter))
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldIntegerDelayedHex(integerProperty: IntegerProperty, filter: Predicate<Int> = Predicate { _ -> true }): TextField {
    val converter = NumberStringConverterForIntegerHex()
    val textField = textFieldDelayed(integerProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyIntegerHexFormatter(textField, integerProperty, filter, converter)
    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding integers in hex format
   */
  @JvmStatic
  fun applyIntegerHexFormatter(textField: TextField, integerProperty: IntegerProperty, filter: Predicate<Int>, converter: NumberStringConverterForIntegerHex) {
    textField.textFormatter = TextFormatter(converter, integerProperty.get(), IntegerHexTextFormatterFilter(converter, filter))
  }

  @JvmStatic
  fun textFieldLongDelayed(longProperty: LongProperty, numberStringConverter: NumberStringConverter): TextField {
    return textFieldLongDelayed(longProperty, Predicate { _ -> true }, numberStringConverter)
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldLongDelayed(longProperty: LongProperty, filter: Predicate<Long> = Predicate { _ -> true }, numberStringConverter: NumberStringConverter = NumberStringConverterForIntegers()): TextField {
    val textField = textFieldDelayed(longProperty, numberStringConverter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyLongFormatter(textField, longProperty, filter, numberStringConverter)
    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldLongDelayedHex(longProperty: LongProperty, filter: Predicate<Long> = Predicate { _ -> true }): TextField {
    val converter = NumberStringConverterForLongHex()
    val textField = textFieldDelayed(longProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyLongHexFormatter(textField, longProperty, filter, converter)
    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding longs
   */
  @JvmStatic
  fun applyLongFormatter(textField: TextField, longProperty: LongProperty, filter: Predicate<Long>, converter: NumberStringConverter) {
    textField.textFormatter = TextFormatter(converter, longProperty.get(), LongTextFormatterFilter(converter, filter))
  }

  /**
   * Adds a text formatter to the text field that only allows adding longs in hex format
   */
  @JvmStatic
  fun applyLongHexFormatter(textField: TextField, longProperty: LongProperty, filter: Predicate<Long>, converter: NumberStringConverterForLongHex) {
    textField.textFormatter = TextFormatter(converter, longProperty.get(), LongHexTextFormatterFilter(converter, filter))
  }

  @JvmStatic
  @JvmOverloads
  fun spinner(valueProperty: IntegerProperty, defaultValue: Int = 0): Spinner<Int> {
    return spinner(valueProperty, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE)
  }

  /**
   * Creates a Spinner instance with the [value factory][Spinner.valueFactoryProperty] set to be an instance
   * of [SpinnerValueFactory.IntegerSpinnerValueFactory].
   *
   * @param valueProperty the property the value property of the spinner factory is bound to.
   * @param minValue      The minimum allowed integer value for the Spinner.
   * @param maxValue      The maximum allowed integer value for the Spinner.
   * @param initialValue  The value of the Spinner when first instantiated.
   */
  @JvmStatic
  fun spinner(valueProperty: IntegerProperty, initialValue: Int, minValue: Int, maxValue: Int): Spinner<Int> {
    val spinner = Spinner<Int>(minValue, maxValue, initialValue)
    val valueFactory = spinner.valueFactory

    spinner.setOnScroll { event ->
      if (spinner.isDisabled) {
        return@setOnScroll
      }
      if (!spinner.isVisible) {
        return@setOnScroll
      }
      val delta = event.deltaY
      if (delta == 0.0) {
        return@setOnScroll
      }
      val increment = if (delta < 0) -1 else +1
      valueFactory.setValue(spinner.value + increment)
    }
    BidirectionalBinding.bindNumber(valueProperty, valueFactory.valueProperty())
    return spinner
  }

  @JvmStatic
  fun placeholder(text: String): Label {
    val placeholder = Label(text)
    placeholder.font = Font(30.0)
    placeholder.textFill = Color.gray(0.4)
    placeholder.textAlignment = TextAlignment.CENTER
    placeholder.alignment = Pos.CENTER
    placeholder.style = "-fx-background-color: #f8f8f8"
    return placeholder
  }

  /**
   * Creates a checkbox that supports three states: All selected / none selected / some selected
   */
  @JvmStatic
  fun checkboxTriState(label: String, callback: TriStateCheckBox.Callback, state: ObservableObjectValue<TriStateCheckboxState>): CheckBox {
    val triStateCheckBox = TriStateCheckBox(label, callback, state)
    return triStateCheckBox.checkBox
  }

  @JvmStatic
  fun checkboxTriState(label: String, callback: TriStateCheckBox.Callback, allSelectedProperty: ReadOnlyBooleanProperty, noneSelectedProperty: ReadOnlyBooleanProperty): CheckBox {
    return checkboxTriState(label, callback, TriStateCheckBox.createStateProperty(allSelectedProperty, noneSelectedProperty))
  }

  @JvmStatic
  fun slider(valueProperty: Property<Number>, minValue: Double, maxValue: Double, step: Double): Slider {
    val slider = Slider()
    slider.min = minValue
    slider.max = maxValue
    slider.majorTickUnit = step
    slider.minorTickCount = 10
    slider.isSnapToTicks = true // skip double values

    com.cedarsoft.commons.javafx.BidirectionalBinding.bindBidirectional(valueProperty,
                                                                        slider.valueProperty(),
                                                                        ChangeListener { _, _, newValue ->
                                                                          // property -> slider
                                                                          slider.valueProperty().set(findClosestStepForValue(newValue.toDouble(), slider.min.toInt().toDouble(), slider.max.toInt().toDouble(), step))
                                                                        },
                                                                        ChangeListener { _, _, newValue ->
                                                                          // slider -> property
                                                                          valueProperty.setValue(findClosestStepForValue(newValue.toDouble(), slider.min.toInt().toDouble(), slider.max.toInt().toDouble(), step))
                                                                        })
    return slider
  }

  /**
   * Wraps the given [ImageView] such that its height is bound to the height of the cell it belongs to (MigLayout)
   *
   * @see .imageViewHolderWithBoundedWidth
   */
  @JvmStatic
  fun imageViewHolderWithBoundedHeight(imageView: ImageView): Node {
    imageView.minHeight(1.0)
    imageView.isPreserveRatio = true
    val imageViewStackPane = StackPane()
    imageViewStackPane.children.add(imageView)
    imageViewStackPane.minHeight = 1.0
    imageView.fitHeightProperty().bind(imageViewStackPane.heightProperty())
    return imageViewStackPane
  }

  /**
   * Wraps the given [ImageView] such that its width is bound to the width of the cell it belongs to (MigLayout)
   *
   * @see .imageViewHolderWithBoundedHeight
   */
  @JvmStatic
  fun imageViewHolderWithBoundedWidth(imageView: ImageView): Node {
    imageView.minWidth(1.0)
    imageView.isPreserveRatio = true
    val imageViewStackPane = StackPane()
    imageViewStackPane.children.add(imageView)
    imageViewStackPane.minWidth = 1.0
    imageView.fitWidthProperty().bind(imageViewStackPane.widthProperty())
    return imageViewStackPane
  }

  @JvmStatic
  fun findClosestStepForValue(value: Double, min: Double, max: Double, step: Double): Double {
    var nextStep = value / step * step
    if (nextStep < min) {
      nextStep = min
    }
    if (nextStep > max) {
      nextStep = max
    }
    return nextStep
  }
}

/**
 * Wraps the node in a scroll pane
 */
fun Node.inScrollPane(): ScrollPane {
  return ScrollPane(this)
}
