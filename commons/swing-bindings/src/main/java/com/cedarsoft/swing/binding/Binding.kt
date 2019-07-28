package com.cedarsoft.swing.binding

import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import java.awt.Color
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.AbstractButton
import javax.swing.BoundedRangeModel
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JSlider
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent

/**
 * Contains binding methods for JavaFX properties to swing components.
 *
 * The order of the arguments (for non bidirectional) bindings is relevant:
 * "The first parameter is bound to the second parameter"
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object Binding

private fun <T, P : Property<T>> getProperty(component: JComponent, key: Any, create: (component: JComponent) -> P): P {
  val foundProperty = component.getClientProperty(key)
  if (foundProperty != null) {
    @Suppress("UNCHECKED_CAST")
    return foundProperty as P
  }

  val property = create(component)
  component.putClientProperty(key, property)
  return property
}

private fun <T> bindValueBidirectional(property: ObjectProperty<T>, comboBox: JComboBox<T>) {
  comboBox.addActionListener {
    @Suppress("UNCHECKED_CAST")
    property.setIfDifferent(comboBox.selectedItem as T)
  }

  property.addListener { _, _, newValue ->
    comboBox.selectedItem = newValue
  }

  @Suppress("UNCHECKED_CAST")
  property.setIfDifferent(comboBox.selectedItem as T)
}

/**
 * Binds the given property to the text of the text field
 */
internal fun bindText(source: JTextComponent, target: Property<String>) {
  source.document.addDocumentListener(object : DocumentListener {
    override fun changedUpdate(e: DocumentEvent?) {
      target.setIfDifferent(source.text)
    }

    override fun insertUpdate(e: DocumentEvent?) {
      target.setIfDifferent(source.text)
    }

    override fun removeUpdate(e: DocumentEvent?) {
      target.setIfDifferent(source.text)
    }
  })

  //Fill property initially
  target.setIfDifferent(source.text)
}

private fun bindText(source: AbstractButton, target: Property<String>) {
  source.addPropertyChangeListener {
    if (it.propertyName == javax.swing.AbstractButton.TEXT_CHANGED_PROPERTY) {
      target.setIfDifferent(source.text)
    }
  }

  //Fill property initially
  target.value = source.text
}

private fun bindTextBidirectional(property: Property<String>, textComponent: JTextComponent) {
  bindText(textComponent, property)
  property.addListener { _, _, newValue ->
    run {
      //Update the text field when the text is different
      if (textComponent.text != newValue) {
        textComponent.text = newValue
      }
    }
  }
}

private fun bindTextBidirectional(property: Property<String>, button: AbstractButton) {
  bindText(button, property)
  property.addListener { _, _, newValue ->
    run {
      //Update the text field when the text is different
      if (button.text != newValue) {
        button.text = newValue
      }
    }
  }
}

/**
 * Binds the text of the label to the given property
 */
private fun bindText(source: ObservableValue<String>, target: JLabel) {
  source.addListener { _, _, newValue -> target.text = newValue }

  //set initially
  target.text = source.value
}

private fun bindDisableBidirectional(disableProperty: Property<Boolean>, component: JComponent) {
  //Update property when the text field changes
  component.addPropertyChangeListener {
    if (it.propertyName == "enabled") {
      disableProperty.setIfDifferent(!component.isEnabled)
    }
  }

  disableProperty.addListener { _, _, newValue ->
    component.isEnabled = !newValue
  }

  //Set initial values
  disableProperty.setIfDifferent(!component.isEnabled)
}

private fun bindMaximumBidirectional(property: IntegerProperty, model: BoundedRangeModel) {
  model.addChangeListener {
    property.setIfDifferent(model.maximum)
  }

  property.addListener { _, _, newValue ->
    model.maximum = newValue.toInt()
  }

  property.setIfDifferent(model.maximum)
}

private fun bindMinimumBidirectional(property: IntegerProperty, model: BoundedRangeModel) {
  model.addChangeListener {
    property.setIfDifferent(model.minimum)
  }

  property.addListener { _, _, newValue ->
    model.minimum = newValue.toInt()
  }

  property.setIfDifferent(model.minimum)
}

private fun bindValueBidirectional(property: IntegerProperty, model: BoundedRangeModel) {
  model.addChangeListener {
    property.setIfDifferent(model.value)
  }

  property.addListener { _, _, newValue ->
    model.value = newValue.toInt()
  }

  property.setIfDifferent(model.value)
}

private fun bindMaximumBidirectional(property: DoubleProperty, model: SpinnerNumberModel) {
  model.addChangeListener {
    property.setIfDifferent((model.maximum as Double?) ?: 0.0)
  }

  property.addListener { _, _, newValue ->
    model.maximum = newValue.toDouble()
  }

  property.setIfDifferent((model.maximum as Double?) ?: 0.0)
}

private fun bindMinimumBidirectional(property: DoubleProperty, model: SpinnerNumberModel) {
  model.addChangeListener {
    property.setIfDifferent((model.minimum as Double?) ?: 0.0)
  }

  property.addListener { _, _, newValue ->
    model.minimum = newValue.toDouble()
  }

  property.setIfDifferent((model.minimum as Double?) ?: 0.0)
}


private fun bindValueBidirectional(property: DoubleProperty, model: SpinnerNumberModel) {
  model.addChangeListener {
    property.setIfDifferent(model.value as Number)
  }

  property.addListener { _, _, newValue ->
    model.value = newValue.toDouble()
  }

  property.setIfDifferent(model.value as Number)
}

private fun bindStepSizeBidirectional(property: DoubleProperty, model: SpinnerNumberModel) {
  model.addChangeListener {
    property.setIfDifferent(model.stepSize as Number)
  }

  property.addListener { _, _, newValue ->
    model.stepSize = newValue.toDouble()
  }

  property.setIfDifferent(model.stepSize as Number)
}

private fun bindIndeterminateBidirectional(property: BooleanProperty, progressBar: JProgressBar) {
  progressBar.addPropertyChangeListener {
    if (it.propertyName == "indeterminate") {
      property.setIfDifferent(progressBar.isIndeterminate)
    }
  }

  property.addListener { _, _, newValue ->
    progressBar.isIndeterminate = newValue
  }

  property.setIfDifferent(progressBar.isIndeterminate)
}

private fun bindFocused(source: JComponent, target: Property<Boolean>) {
  //Update property when the text field changes
  source.addFocusListener(object : FocusListener {
    override fun focusLost(e: FocusEvent?) {
      target.setIfDifferent(false)
    }

    override fun focusGained(e: FocusEvent?) {
      target.setIfDifferent(true)
    }
  })

  //Set initial values
  target.setIfDifferent(source.hasFocus())
}

/**
 * Binds the selected state bidirectional
 */
private fun bindSelectedBidirectional(property: Property<Boolean>, button: AbstractButton) {
  button.addActionListener { property.value = button.isSelected }
  property.addListener { _, _, newValue -> button.isSelected = newValue }

  //Set initially
  property.setIfDifferent(button.isSelected)
}

private fun bindEditableBidirectional(property: Property<Boolean>, comboBox: JComboBox<*>) {
  comboBox.addPropertyChangeListener {
    if (it.propertyName == "editable") {
      property.setIfDifferent(comboBox.isEditable)
    }
  }

  property.addListener { _, _, newValue -> comboBox.isEditable = newValue }

  //Set initially
  property.setIfDifferent(comboBox.isEditable)
}

private fun bindVisibleBidirectional(property: Property<Boolean>, component: JComponent) {
  component.addComponentListener(object : ComponentListener {
    override fun componentMoved(e: ComponentEvent?) {
    }

    override fun componentResized(e: ComponentEvent?) {
    }

    override fun componentHidden(e: ComponentEvent?) {
      property.setIfDifferent(false)
    }

    override fun componentShown(e: ComponentEvent?) {
      property.setIfDifferent(true)
    }
  })

  property.addListener { _, _, newValue -> component.isVisible = newValue }

  //Set initially
  property.setIfDifferent(component.isVisible)
}

private fun bindForegroundBidirectional(property: Property<Color>, component: JComponent) {
  component.addPropertyChangeListener {
    if (it.propertyName == "foreground") {
      property.setIfDifferent(component.foreground)
    }
  }

  property.addListener { _, _, newValue -> component.foreground = newValue }

  //Set initially
  property.setIfDifferent(component.foreground)
}

private fun bindBackgroundBidirectional(property: Property<Color>, component: JComponent) {
  component.addPropertyChangeListener {
    if (it.propertyName == "background") {
      property.setIfDifferent(component.background)
    }
  }

  property.addListener { _, _, newValue -> component.background = newValue }

  //Set initially
  property.setIfDifferent(component.background)
}

private const val TEXT_PROPERTY_KEY: String = "textPropertyKey"
private const val FOREGROUND_PROPERTY_KEY: String = "foregroundPropertyKey"
private const val VISIBLE_PROPERTY_KEY: String = "visiblePropertyKey"
private const val BACKGROUND_PROPERTY_KEY: String = "backgroundPropertyKey"
private const val SELECTED_PROPERTY_KEY: String = "selectedPropertyKey"
private const val DISABLE_PROPERTY_KEY: String = "disablePropertyKey"
private const val FOCUSED_PROPERTY_KEY: String = "focusedPropertyKey"
private const val MAXIMUM_PROPERTY_KEY: String = "maximumPropertyKey"
private const val MINIMUM_PROPERTY_KEY: String = "minimumPropertyKey"
private const val VALUE_PROPERTY_KEY: String = "valuePropertyKey"
private const val STEP_SIZE_PROPERTY_KEY: String = "stepSizePropertyKey"
private const val INDETERMINATE_PROPERTY_KEY: String = "indeterminatePropertyKey"
private const val EDITABLE_PROPERTY_KEY: String = "editablePropertyKey"

/**
 * Returns the property that contains the disabled property
 */

fun <T> JComboBox<T>.editableProperty(): BooleanProperty {
  return getProperty(this, EDITABLE_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "editable")
    bindEditableBidirectional(property, this)
    property
  }
}


fun <T> JComboBox<T>.items(): ObservableList<T> {
  val model = this.model
  if (model !is ComboBoxObservableListModel) {
    throw IllegalArgumentException("Combo box has invalid model. Requires a <ComboBoxObservableListModel> but was <$model>")
  }

  return model.items
}

/**
 * Returns the value property for combo boxes that are *not* editable.
 * Editable combo boxes will throw a class cast exception if this property is used.
 */
fun <T> JComboBox<T>.valueProperty(): ObjectProperty<T> {
  return getProperty(this, VALUE_PROPERTY_KEY) {
    val property = SimpleObjectProperty<T>(this, "value")
    bindValueBidirectional(property, this)
    property
  }
}

/**
 * Returns the value property for editable combo boxes.
 * The property may contain <T> or String
 */

fun <T> JComboBox<T>.valuePropertyEditable(): ObjectProperty<*> {
  return valueProperty()
}


fun JProgressBar.indeterminateProperty(): BooleanProperty {
  return getProperty(this, INDETERMINATE_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "indeterminate")
    bindIndeterminateBidirectional(property, this)
    property
  }
}

/**
 * Returns the maximum property for the progress bar
 */

fun JProgressBar.maximumProperty(): IntegerProperty {
  return getProperty(this, MAXIMUM_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "maximum")
    bindMaximumBidirectional(property, model)
    property
  }
}


fun JProgressBar.minimumProperty(): IntegerProperty {
  return getProperty(this, MINIMUM_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "minimum")
    bindMinimumBidirectional(property, model)
    property
  }
}


fun JProgressBar.valueProperty(): IntegerProperty {
  return getProperty(this, VALUE_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "value")
    bindValueBidirectional(property, model)
    property
  }
}


fun JSlider.maximumProperty(): IntegerProperty {
  return getProperty(this, MAXIMUM_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "maximum")
    bindMaximumBidirectional(property, model)
    property
  }
}


fun JSlider.minimumProperty(): IntegerProperty {
  return getProperty(this, MINIMUM_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "minimum")
    bindMinimumBidirectional(property, model)
    property
  }
}


fun JSlider.valueProperty(): IntegerProperty {
  return getProperty(this, VALUE_PROPERTY_KEY) {
    val property = SimpleIntegerProperty(this, "value")
    bindValueBidirectional(property, model)
    property
  }
}

fun JSpinner.maximumProperty(): DoubleProperty {
  return getProperty(this, MAXIMUM_PROPERTY_KEY) {
    val property = SimpleDoubleProperty(this, "maximum")
    bindMaximumBidirectional(property, model as SpinnerNumberModel)
    property
  }
}


fun JSpinner.minimumProperty(): DoubleProperty {
  return getProperty(this, MINIMUM_PROPERTY_KEY) {
    val property = SimpleDoubleProperty(this, "minimum")
    bindMinimumBidirectional(property, model as SpinnerNumberModel)
    property
  }
}


fun JSpinner.valueProperty(): DoubleProperty {
  return getProperty(this, VALUE_PROPERTY_KEY) {
    val property = SimpleDoubleProperty(this, "value")
    bindValueBidirectional(property, model as SpinnerNumberModel)
    property
  }
}

fun JSpinner.stepSizeProperty(): DoubleProperty {
  return getProperty(this, STEP_SIZE_PROPERTY_KEY) {
    val property = SimpleDoubleProperty(this, "stepSize")
    bindStepSizeBidirectional(property, model as SpinnerNumberModel)
    property
  }
}

/**
 * Returns the text property of the given text component
 */

fun JLabel.textProperty(): StringProperty {
  return getProperty(this, TEXT_PROPERTY_KEY) {
    val property = SimpleStringProperty(this, "text")
    bindText(property, this)
    property
  }
}

/**
 * Returns the text property of the given text component
 */
fun JTextComponent.textProperty(): StringProperty {
  return getProperty(this, TEXT_PROPERTY_KEY) {
    val property = SimpleStringProperty(this, "text")
    bindTextBidirectional(property, this)
    property
  }
}

fun JComponent.foregroundProperty(): ObjectProperty<Color> {
  return getProperty(this, FOREGROUND_PROPERTY_KEY) {
    val property = SimpleObjectProperty<Color>(this, "foreground", Color.BLACK)
    bindForegroundBidirectional(property, this)
    property
  }
}

fun JComponent.visibleProperty(): BooleanProperty {
  return getProperty(this, VISIBLE_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "visible")
    bindVisibleBidirectional(property, this)
    property
  }
}

fun JComponent.backgroundProperty(): ObjectProperty<Color> {
  return getProperty(this, BACKGROUND_PROPERTY_KEY) {
    val property = SimpleObjectProperty<Color>(this, "background", Color.BLACK)
    bindBackgroundBidirectional(property, this)
    property
  }
}

fun AbstractButton.textProperty(): StringProperty {
  return getProperty(this, TEXT_PROPERTY_KEY) {
    val property = SimpleStringProperty(this, "text")
    bindTextBidirectional(property, this)
    property
  }
}

/**
 * Returns the property that holds the selected state
 */

fun AbstractButton.selectedProperty(): BooleanProperty {
  return getProperty(this, SELECTED_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "selected")
    bindSelectedBidirectional(property, this)
    property
  }
}

/**
 * Returns the property that contains the disabled property
 */

fun JComponent.disableProperty(): BooleanProperty {
  return getProperty(this, DISABLE_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "disable")
    bindDisableBidirectional(property, this)
    property
  }
}

/**
 * Returns the property that contains the focused property
 */

fun JComponent.focusedProperty(): ReadOnlyBooleanProperty {
  return getProperty(this, FOCUSED_PROPERTY_KEY) {
    val property = SimpleBooleanProperty(this, "focused")
    bindFocused(this, property)
    property
  }
}

/**
 * Sets the value if the current value of the property is not equal
 */
private fun <T> Property<T>.setIfDifferent(newValue: T) {
  if (isBound) {
    //Skip writing the value if the property is bound
    return
  }

  if (value != newValue) {
    value = newValue
  }
}
