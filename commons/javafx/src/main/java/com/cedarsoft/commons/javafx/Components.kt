package com.cedarsoft.commons.javafx

import com.cedarsoft.charting.annotations.JavaFriendly
import com.cedarsoft.commons.javafx.combo.ItemThatMayBeDisabled
import com.cedarsoft.commons.javafx.combo.ListViewListCellCallbackForItemThatMayBeDisabled
import com.cedarsoft.commons.javafx.properties.*
import com.cedarsoft.unit.other.px
import com.google.common.collect.ImmutableList
import com.sun.javafx.binding.BidirectionalBinding
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerBinding
import javafx.beans.binding.IntegerExpression
import javafx.beans.binding.LongExpression
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyFloatProperty
import javafx.beans.property.ReadOnlyLongProperty
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableNumberValue
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBox
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat
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

  /**
   * Creates an empty pane with the size 0/0
   */
  @JvmStatic
  fun empty(): Pane {
    return Pane().also {
      it.prefWidth = 0.0
      it.prefHeight = 0.0
      it.maxWidth = 0.0
      it.maxHeight = 0.0
      it.minWidth = 0.0
      it.minHeight = 0.0
    }
  }

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

  @JvmStatic
  @JvmOverloads
  fun labelBold(text: String = ""): Label {
    return minWidth2prefLabel(text).also {
      val currentFont = it.font
      it.font = Font.font(currentFont.name, FontWeight.BOLD, currentFont.size)
    }
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
  fun headline2(headline: ObservableValue<String>): Label {
    val label = label(headline)
    label.styleClass.add("headline-2")
    return label
  }

  @JvmStatic
  fun checkBox(text: String, selected: BooleanPropertyWithWritableState): CheckBox {
    return checkBox(text, selected.property).also {
      it.disableProperty().bind(selected.writableProperty.not())
    }
  }

  @JvmStatic
  fun checkBox(text: String, selected: Property<Boolean>): CheckBox {
    val checkBox = CheckBox(text)
    checkBox.selectedProperty().bindBidirectional(selected)
    return checkBox
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  fun <T> createRadioButtons(property: Property<T>, values: Collection<T>): HBox {
    return createRadioButtons<T>(null, property, values.stream())
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  @SafeVarargs
  fun <T> createRadioButtons(property: Property<T>, vararg values: T): HBox {
    return createRadioButtons<T>(null, property, Arrays.stream<T>(values))
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  @SafeVarargs
  fun <T> createRadioButtons(label: String?, property: Property<T>, vararg values: T): HBox {
    return createRadioButtons(label, property, Arrays.stream<T>(values))
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  fun <T> createRadioButtons(label: String?, property: Property<T>, values: Stream<out T>): HBox {
    val buttons = createRadioButtonsElements(property, values)

    val hBox = HBox()
    if (label != null) {
      hBox.children.add(Label(label))
    }
    hBox.children.addAll(buttons)
    return hBox
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  fun <T> createRadioButtonsElements(property: Property<T>, values: List<out T>): List<RadioButton> {
    return createRadioButtonsElements(property, values.stream())
  }

  @Deprecated("use radioButtons instead")
  @JvmStatic
  fun <T> createRadioButtonsElements(property: Property<T>, values: Stream<out T>): List<RadioButton> {
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

    return buttons
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
    val radioButton = RadioButton(EnumTranslatorUtil.translate(value))
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

  /**
   * Uses all enum values
   */
  @JvmStatic
  inline fun <reified E : Enum<E>> comboBox(enumProperty: Property<E>): ComboBox<E> {
    return comboBox(enumProperty, enumValues())
  }

  @JvmStatic
  fun <E : Enum<E>> comboBox(enumProperty: Property<E>, values: Array<E>): ComboBox<E> {
    val comboBox = ComboBox(FXCollections.observableArrayList(*values))
    comboBox.valueProperty().bindBidirectional(enumProperty)
    EnumListCell.createFor(comboBox)
    return comboBox
  }

  @JvmStatic
  fun <T> comboBox(property: Property<T>, values: Array<T>, converter: (T) -> String = { t -> t.toString() }): ComboBox<T> {
    return comboBox(property, FXCollections.observableArrayList(values.asList()), converter)
  }

  @JvmStatic
  fun <T> comboBox(property: Property<T>, values: List<T>, converter: (T) -> String = { t -> t.toString() }): ComboBox<T> {
    return comboBox(property, FXCollections.observableArrayList(values), converter)
  }

  @JvmOverloads
  @JvmStatic
  fun <T> comboBox(property: Property<T>, values: ObservableList<T>, converter: (T) -> String = { t -> t.toString() }): ComboBox<T> {
    val comboBox = ComboBox(values)
    comboBox.valueProperty().bindBidirectional(property)
    comboBox.formatUsingConverter(converter)
    return comboBox
  }

  /**
   * Creates a new combo box with optional items
   */
  @JvmStatic
  fun <T> comboBoxWithOptionalItems(property: Property<T>, options: ObservableList<ItemThatMayBeDisabled<T>>, converter: (T) -> String): ComboBox<ItemThatMayBeDisabled<T>> {
    val comboBox = ComboBox(options)

    com.cedarsoft.commons.javafx.BidirectionalBinding.bindBidirectional(
      property, comboBox.valueProperty(),
      { _, _, newValue ->
        comboBox.valueProperty().value = options.find {
          it.value == newValue
        } ?: throw IllegalStateException("Not found")
      },
      { _, _, newValue ->
        property.value = newValue.value
      }
    )

    comboBox.formatUsingConverter {
      converter(it.value)
    }

    comboBox.cellFactory = ListViewListCellCallbackForItemThatMayBeDisabled(converter)

    return comboBox
  }

  @JvmStatic
  fun <T> choiceBox(property: Property<T>, values: List<T>, converter: (T) -> String): ChoiceBox<T> {
    val choiceBox = ChoiceBox(FXCollections.observableArrayList(values))
    choiceBox.valueProperty().bindBidirectional(property)
    choiceBox.converter = object : OneWayConverter<T>() {
      override fun format(toFormat: T): String {
        return converter(toFormat)
      }
    }
    return choiceBox
  }

  @JvmStatic
  fun <E : Enum<E>> choiceBox(property: Property<E>, values: Array<E>): ChoiceBox<E> {
    val choiceBox = ChoiceBox(FXCollections.observableArrayList(*values))
    choiceBox.valueProperty().bindBidirectional(property)

    choiceBox.converter = object : OneWayConverter<E>() {
      override fun format(toFormat: E): String {
        return EnumTranslatorUtil.translate(toFormat)
      }
    }
    return choiceBox
  }

  @JvmStatic
  fun <T> choiceBox(property: Property<T>, values: ObservableList<T>, converter: (T) -> String): ChoiceBox<T> {
    val choiceBox = ChoiceBox(values)
    choiceBox.valueProperty().bindBidirectional(property)

    choiceBox.converter = object : OneWayConverter<T>() {
      override fun format(toFormat: T): String {
        return converter(toFormat)
      }
    }
    return choiceBox
  }

  @JvmStatic
  fun createCheckBoxReadOnly(label: String, selectedProperty: Property<Boolean>): CheckBox {
    val checkBox = CheckBox(label)
    checkBox.selectedProperty().bind(selectedProperty)
    checkBox.addEventFilter(EventType.ROOT) { it.consume() }
    return checkBox
  }

  @JvmStatic
  fun createCheckBox(label: String, selectedProperty: Property<Boolean>): CheckBox {
    val checkBox = CheckBox(label)
    checkBox.selectedProperty().bindBidirectional(selectedProperty)
    return checkBox
  }

  @JvmStatic
  fun createCheckBox(label: String, selectedProperty: BooleanPropertyWithWritableState): CheckBox {
    return createCheckBox(label, selectedProperty.property).also {
      it.disableProperty().bind(selectedProperty.writableProperty.not())
    }
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
  fun button(text: String?, onAction: (ActionEvent) -> Unit): Button {
    val button = Button(text)
    button.onAction = EventHandler {
      onAction(it)
    }
    return button
  }

  @JvmStatic
  fun button(text: String?, disabled: ObservableValue<Boolean>, onAction: (ActionEvent) -> Unit): Button {
    val button = Button(text)
    button.onAction = EventHandler {
      onAction(it)
    }
    button.disableProperty().bind(disabled)
    return button
  }

  @JvmStatic
  fun link(text: String, onAction: (ActionEvent) -> Unit): Hyperlink {
    val link = Hyperlink(text)
    link.onAction = EventHandler {
      onAction(it)
    }
    return link
  }

  @JavaFriendly
  @JvmStatic
  fun link(text: String, onAction: Consumer<ActionEvent>): Hyperlink {
    return link(text) {
      onAction.accept(it)
    }
  }

  @JvmStatic
  fun toggleButton(text: String, selected: Property<Boolean>): ToggleButton {
    val button = ToggleButton(text)
    button.selectedProperty().bindBidirectional(selected)
    return button
  }

  @JvmStatic
  fun textArea(text: Property<String>): TextArea {
    val textArea = TextArea()
    textArea.textProperty().bindBidirectional(text)
    return textArea
  }

  @JvmStatic
  fun textArea(text: String): TextArea {
    val textArea = TextArea()
    textArea.textProperty().set(text)
    return textArea
  }

  @JvmStatic
  fun textField(text: Property<String>): TextField {
    val textField = TextField()
    textField.textProperty().bindBidirectional(text)
    return textField
  }

  @JvmStatic
  fun textField(text: PropertyWithWritableState<String>): TextField {
    return textField(text.property).also {
      it.disableProperty().bind(text.writableProperty.not())
    }
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

  @JvmStatic
  fun textFieldDelayed(text: PropertyWithWritableState<String>): TextField {
    return textFieldDelayed(text.property).also {
      it.disableProperty().bind(text.writableProperty.not())
    }
  }

  /**
   * Creates a text field with a [DelayedTextFieldBinding].
   *
   * @param textProperty      the property the text field's text property is bidirectionally bound to
   * @param converter its `toString`-method is called to convert the value of the property to the text of the text field;
   * its `fromString`-method is called to convert the text of the text field to the value of the property.
   */
  @JvmStatic
  fun <T> textFieldDelayed(textProperty: Property<T>, converter: StringConverter<T>): TextField {
    val textField = TextField()
    DelayedTextFieldBinding.connect(textField, textProperty, converter)

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = textProperty

    return textField
  }

  @JvmStatic
  fun <E : Enum<E>> textFieldEnumReadOnly(enumProperty: ObservableValue<E>): TextField {
    val textField = TextField()
    textField.isEditable = false
    textField.textProperty().bind(Bindings.createStringBinding({ EnumTranslatorUtil.translate(enumProperty.value) }, enumProperty))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = enumProperty

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
  fun textFieldReadonly(observableText: ObservableValue<out String>): TextField {
    val textField = TextField()
    textField.textProperty().bind(observableText)
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

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = longProperty

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

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = floatProperty

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

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = floatProperty

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
  fun textFieldDoubleReadonly(observableValue: ObservableDoubleValue, floatConverter: NumberStringConverterForFloatingPointNumbers = NumberStringConverterForFloatingPointNumbers()): TextField {
    val textField = TextField()
    textField.alignmentProperty().set(Pos.TOP_RIGHT)
    observableValue.addListener { _, _, newValue -> textField.text = floatConverter.toString(newValue) }
    textField.text = floatConverter.toString(observableValue.get())
    textField.isEditable = false

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = observableValue

    return textField
  }

  @JvmStatic
  fun textFieldDoubleDelayed(doubleProperty: DoubleProperty, converter: NumberStringConverterForFloatingPointNumbers): TextField {
    return textFieldDoubleDelayed(doubleProperty, { _ -> true }, converter)
  }

  /**
   * ATTENTION!
   * Changes to [decimals] do *not* trigger a rerender!
   */
  @JvmStatic
  fun textFieldDoubleDelayed(doubleProperty: DoubleProperty, decimals: IntegerBinding): TextField {
    val numberForBinding = decimals.map { decimalsCount ->
      val numberFormat = NumberFormat.getNumberInstance().also {
        it.maximumFractionDigits = decimalsCount.toInt()
        it.minimumFractionDigits = decimalsCount.toInt()
      }
      numberFormat
    }

    return textFieldDoubleDelayed(doubleProperty, { _ -> true }, NumberStringConverterForFloatingPointNumbers() {
      numberForBinding.value
    })
  }

  fun textFieldDoubleDelayed(doubleProperty: DoubleProperty, decimals: Int = 2): TextField {
    val numberFormat = NumberFormat.getNumberInstance().also {
      it.maximumFractionDigits = decimals
      it.minimumFractionDigits = decimals
    }
    return textFieldDoubleDelayed(doubleProperty, { _ -> true }, NumberStringConverterForFloatingPointNumbers(numberFormat))
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldDoubleDelayed(
    doubleProperty: DoubleProperty,
    filter: Predicate<Double> = Predicate { _ -> true },
    converter: NumberStringConverterForFloatingPointNumbers = NumberStringConverterForFloatingPointNumbers()
  ): TextField {
    val textField = textFieldDelayed(doubleProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyDoubleFormatter(textField, doubleProperty, filter, converter)

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = doubleProperty

    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldDoubleDelayed(
    doubleProperty: DoublePropertyWithWritableState,
    filter: Predicate<Double> = Predicate { _ -> true },
    converter: NumberStringConverterForFloatingPointNumbers = NumberStringConverterForFloatingPointNumbers()
  ): TextField {
    return textFieldDoubleDelayed(doubleProperty.property, filter, converter).also {
      it.disableProperty().bind(doubleProperty.writableProperty.not())
    }
  }

  /**
   * Adds a text formatter to the text field that only allows adding doubles
   */
  @JvmStatic
  fun applyDoubleFormatter(textField: TextField, doubleProperty: DoubleProperty, filter: Predicate<Double>, converter: NumberStringConverterForFloatingPointNumbers) {
    textField.textFormatter = TextFormatter(converter, doubleProperty.get(), DoubleTextFormatterFilter(converter, filter))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = doubleProperty
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

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = integerProperty

    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldIntegerDelayed(integerProperty: IntegerPropertyWithWritableState, filter: Predicate<Int> = Predicate { _ -> true }, converter: NumberStringConverter = NumberStringConverterForIntegers()): TextField {
    return textFieldIntegerDelayed(integerProperty.property, filter, converter).also {
      it.disableProperty().bind(integerProperty.writableProperty.not())
    }
  }

  /**
   * Adds a text formatter to the text field that only allows adding integers
   */
  @JvmStatic
  fun applyIntegerFormatter(textField: TextField, integerProperty: IntegerProperty, filter: Predicate<Int>, converter: NumberStringConverter) {
    textField.textFormatter = TextFormatter(converter, integerProperty.get(), IntegerTextFormatterFilter(converter, filter))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = integerProperty
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldIntegerDelayedHex(integerProperty: IntegerProperty, filter: Predicate<Int> = Predicate { _ -> true }): TextField {
    val converter = NumberStringConverterForIntegerHex()
    val textField = textFieldDelayed(integerProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyIntegerHexFormatter(textField, integerProperty, filter, converter)

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = integerProperty

    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding integers in hex format
   */
  @JvmStatic
  fun applyIntegerHexFormatter(textField: TextField, integerProperty: IntegerProperty, filter: Predicate<Int>, converter: NumberStringConverterForIntegerHex) {
    textField.textFormatter = TextFormatter(converter, integerProperty.get(), IntegerHexTextFormatterFilter(converter, filter))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = integerProperty
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

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = longProperty

    return textField
  }

  @JvmStatic
  @JvmOverloads
  fun textFieldLongDelayedHex(longProperty: LongProperty, filter: Predicate<Long> = Predicate { _ -> true }): TextField {
    val converter = NumberStringConverterForLongHex()
    val textField = textFieldDelayed(longProperty, converter)
    textField.alignmentProperty().set(Pos.CENTER_RIGHT)
    applyLongHexFormatter(textField, longProperty, filter, converter)

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = longProperty

    return textField
  }

  /**
   * Adds a text formatter to the text field that only allows adding longs
   */
  @JvmStatic
  fun applyLongFormatter(textField: TextField, longProperty: LongProperty, filter: Predicate<Long>, converter: NumberStringConverter) {
    textField.textFormatter = TextFormatter(converter, longProperty.get(), LongTextFormatterFilter(converter, filter))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = longProperty
  }

  /**
   * Adds a text formatter to the text field that only allows adding longs in hex format
   */
  @JvmStatic
  fun applyLongHexFormatter(textField: TextField, longProperty: LongProperty, filter: Predicate<Long>, converter: NumberStringConverterForLongHex) {
    textField.textFormatter = TextFormatter(converter, longProperty.get(), LongHexTextFormatterFilter(converter, filter))

    //Avoid premature gc of the property
    textField.properties["observableBoundToText"] = longProperty
  }

  @JvmStatic
  @JvmOverloads
  fun spinner(valueProperty: IntegerProperty, defaultValue: Int = valueProperty.get()): Spinner<Int> {
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

    //Editable manually
    spinner.isEditable = true

    //It is necessary to write the value on focus lost
    spinner.focusedProperty().consume { focused ->
      if (!focused) {
        if (valueFactory != null) {
          val converter = valueFactory.converter
          if (converter != null) {
            val value = converter.fromString(spinner.editor.text)
            valueFactory.value = value
          }
        }
      }
    }

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

  /**
   * Creates a slider and text field
   */
  fun sliderWithTextField(valueProperty: DoubleProperty, minValue: ObservableNumberValue, maxValue: ObservableNumberValue, step: Double? = null, decimals: Int = 2): HBox {
    val valueSlider = slider(valueProperty, minValue, maxValue, step)
    val valueTextField = textFieldDoubleDelayed(valueProperty, decimals)

    HBox.setHgrow(valueSlider, Priority.ALWAYS)

    return hbox(5, valueSlider, valueTextField)
  }

  fun sliderWithTextField(valueProperty: DoubleProperty, minValue: Double, maxValue: Double, step: Double? = null): HBox {
    return hbox(
      5,
      slider(valueProperty, minValue, maxValue, step),
      textFieldDoubleDelayed(valueProperty)
    )
  }

  fun slider(valueProperty: Property<Number>, range: IntRange): Slider {
    return slider(valueProperty, range.first.toDouble(), range.last.toDouble(), range.step.toDouble())
  }

  @JvmStatic
  fun slider(valueProperty: Property<Number>, minValue: ObservableNumberValue, maxValue: ObservableNumberValue, step: Double? = null): Slider {
    return slider(valueProperty, minValue.doubleValue(), maxValue.doubleValue(), step).also {
      it.minProperty().bind(minValue)
      it.maxProperty().bind(maxValue)
    }
  }

  @JvmStatic
  fun slider(valueProperty: Property<Number>, minValue: Double, maxValue: Double, step: Double? = null): Slider {
    val slider = Slider()
    slider.min = minValue
    slider.max = maxValue

    //check for plausibility of step size
    if (step != null) {
      val tickCount = (maxValue - minValue) / step
      require(tickCount <= 10_000) {
        "Step too small. Was $step but results in $tickCount ticks for min: $minValue - max: $maxValue"
      }

      slider.majorTickUnit = step
      slider.minorTickCount = 10
      slider.isSnapToTicks = true // skip double values
    }

    bindBidirectional(valueProperty, slider.valueProperty()) {
      a2b = { newValue ->
        findClosestStepForValue(newValue.toDouble(), slider.min, slider.max, step)
      }
      b2a = { newValue ->
        findClosestStepForValue(newValue.toDouble(), slider.min, slider.max, step)
      }
    }

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
  fun imageViewHolderWithBoundedWidthAndHeight(imageView: ImageView): Node {
    imageView.minWidth(1.0)
    imageView.isPreserveRatio = true

    val imageViewStackPane = StackPane()
    imageViewStackPane.children.add(imageView)
    imageViewStackPane.minWidth = 1.0
    imageView.fitWidthProperty().bind(imageViewStackPane.widthProperty())
    imageView.fitHeightProperty().bind(imageViewStackPane.heightProperty())
    return imageViewStackPane
  }

  @JvmStatic
  fun findClosestStepForValue(value: Double, min: Double, max: Double, step: Double?): Double {
    if (step == null) {
      return value.coerceIn(min, max)
    }

    var nextStep = value / step * step
    if (nextStep < min) {
      nextStep = min
    }
    if (nextStep > max) {
      nextStep = max
    }
    return nextStep
  }

  @JvmStatic
  fun colorPicker(colorProperty: PropertyWithWritableState<Color>): ColorPicker {
    return colorPicker(colorProperty.property).also {
      it.disableProperty().bind(colorProperty.writableProperty.not())
    }
  }

  /**
   * Creates a color picker
   */
  @JvmStatic
  fun colorPicker(property: Property<Color>): ColorPicker {
    return ColorPicker().also {
      it.valueProperty().bindBidirectional(property)
    }
  }


  /**
   * Wraps the node in a scroll pane
   */
  @JvmStatic
  @JavaFriendly
  fun inScrollPane(content: Node): ScrollPane {
    return content.inScrollPane()
  }

  /**
   * Adds the given elements within a stacked pane
   */
  fun stacked(vararg children: Node): StackPane {
    return StackPane(*children)
  }
}

/**
 * Wraps the node in a scroll pane
 */
fun Node.inScrollPane(): ScrollPane {
  return ScrollPane(this).also {
    it.isFitToHeight = true
    it.isFitToWidth = true
  }
}

/**
 * Binds the max property
 */
fun Slider.withMax(max: ObservableValue<Number>): Slider {
  this.maxProperty().bind(max)
  return this
}
