package it.neckar.react.common.form

import com.cedarsoft.common.kotlin.lang.parseInt
import com.cedarsoft.formatting.format
import com.cedarsoft.i18n.I18nConfiguration
import it.neckar.commons.kotlin.js.safeGet
import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.LABEL
import kotlinx.html.TEXTAREA
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.role
import kotlinx.html.title
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*


fun RBuilder.checkbox(
  /**
   * The current value
   */
  value: Boolean,
  /**
   * Is called on change
   */
  onChange: OnChange<Boolean>,
  /**
   * The field name
   */
  fieldName: String,
  /**
   * The title
   */
  title: String,

  editableStatus: EditableStatus,

  labelConfig: (RDOMBuilder<LABEL>.() -> Unit)? = null,
  checkboxConfig: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(checkbox) {
  attrs {
    this.value = value
    this.onChange = onChange
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.labelConfig = labelConfig
    this.checkboxConfig = checkboxConfig
  }
}

fun RBuilder.checkbox(
  /**
   * The current value
   */
  valueAndSetter: StateInstance<Boolean>,
  /**
   * The field name
   */
  fieldName: String,
  /**
   * The title
   */
  title: String,

  editableStatus: EditableStatus = Editable,

  labelConfig: (RDOMBuilder<LABEL>.() -> Unit)? = null,
  checkboxConfig: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  checkbox(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    labelConfig = labelConfig,
    checkboxConfig = checkboxConfig,
  )
}

val checkbox: FC<CheckboxProps> = fc("checkbox") { props ->
  val uniqueId = uniqueIdMemo("checkbox")


  div(classes = "form-check") {

    input(name = props.fieldName, type = InputType.checkBox, classes = "form-check-input") {
      attrs {
        id = uniqueId

        placeholder = props.title
        title = props.title
        checked = props.value
        //Workaround to avoid warning on console
        // https://github.com/JetBrains/kotlin-wrappers/issues/35
        // https://github.com/Kotlin/kotlinx.html/issues/110
        set("checked", props.value)

        disabled = props.editableStatus == ReadOnly

        onChangeFunction = {
          val inputElement = it.target as HTMLInputElement
          props.onChange(inputElement.checked)
        }
      }

      props.checkboxConfig?.invoke(this)
    }

    label(classes = "form-check-label") {
      +props.title

      attrs {
        htmlForFixed = uniqueId
      }

      props.labelConfig?.invoke(this)
    }

  }
}


external interface CheckboxProps : Props {
  /**
   * The current value
   */
  var value: Boolean

  /**
   * Is called on change
   */
  var onChange: OnChange<Boolean>

  /**
   * The field name
   */
  var fieldName: String

  /**
   * The title
   */
  var title: String

  var editableStatus: EditableStatus

  var labelConfig: ((RDOMBuilder<LABEL>) -> Unit)?
  var checkboxConfig: ((RDOMBuilder<INPUT>) -> Unit)?
}


fun RBuilder.buttonGroup(classes: String? = null, config: (RDOMBuilder<DIV>.() -> Unit)) {
  div(classes = "btn-group") {
    attrs {
      role = "group"
      classes?.let { addClass(it) }
    }

    config(this)
  }
}


fun RBuilder.radioButton(
  buttonId: String,
  /**
   * The current value
   */
  value: Boolean,
  /**
   * Is called on change
   */
  onChange: OnChange<Boolean>,

  editableStatus: EditableStatus,

  inputClasses: String = "btn btn-outline-primary",

  inputConfig: (RDOMBuilder<INPUT>.() -> Unit)? = null,
  labelConfig: (RDOMBuilder<LABEL>.() -> Unit)?,

  ): Unit = child(RadioButton) {
  attrs {
    this.buttonId = buttonId
    this.value = value
    this.onChange = onChange
    this.editableStatus = editableStatus
    this.inputClasses = inputClasses
    this.inputConfig = inputConfig
    this.labelConfig = labelConfig
  }
}

fun RBuilder.radioButton(
  buttonId: String,
  /**
   * The current value
   */
  valueAndSetter: StateInstance<Boolean>,

  editableStatus: EditableStatus = Editable,

  inputClasses: String = "btn btn-outline-primary",

  inputConfig: (RDOMBuilder<INPUT>.() -> Unit)? = null,
  labelConfig: (RDOMBuilder<LABEL>.() -> Unit)?,

  ) {
  radioButton(
    buttonId = buttonId,
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    editableStatus = editableStatus,
    inputClasses = inputClasses,
    inputConfig = inputConfig,
    labelConfig = labelConfig,
  )
}

val RadioButton: FC<RadioButtonProps> = fc("RadioButton") { props ->
  val buttonId = props::buttonId.safeGet()
  val value = props::value.safeGet()
  val onChange = props::onChange.safeGet()
  val editableStatus = props::editableStatus.safeGet()
  val buttonClasses = props::inputClasses.safeGet()
  val inputConfig = props::inputConfig.safeGet()
  val labelConfig = props::labelConfig.safeGet()


  input(type = InputType.radio, classes = "btn-check") {
    attrs {
      id = buttonId
      autoComplete = false

      // Workaround to avoid warning on console
      // https://github.com/JetBrains/kotlin-wrappers/issues/35
      // https://github.com/Kotlin/kotlinx.html/issues/110
      set("checked", value)

      disabled = editableStatus == ReadOnly

      onChangeFunction = {
        val inputElement = it.target as HTMLInputElement
        onChange(inputElement.checked)
      }
    }

    inputConfig?.invoke(this)
  }
  label(classes = buttonClasses) {
    attrs {
      htmlForFixed = buttonId
    }

    labelConfig?.invoke(this)
  }

}

external interface RadioButtonProps : Props {
  var buttonId: String

  /**
   * The current value
   */
  var value: Boolean

  /**
   * Is called on change
   */
  var onChange: OnChange<Boolean>

  var editableStatus: EditableStatus

  var inputClasses: String
  var inputConfig: ((RDOMBuilder<INPUT>) -> Unit)?
  var labelConfig: ((RDOMBuilder<LABEL>) -> Unit)?
}


/**
 * Creates an input field. The value and updates are handled using a [StateInstance]
 */
fun RBuilder.inputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String>,

  fieldName: String,
  title: String,

  /**
   * The (optional) placeholder - if no placeholder is provided, the title is used
   */
  placeholder: String? = null,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ) {
  inputField(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    placeholder = placeholder,
    config = config,
  )
}

fun RBuilder.intInput(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<Int>,
  fieldName: String,
  title: String,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  intInput(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    numberConstraint = Unconstraint,
    config = config,
  )
}

fun RBuilder.intInput(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  value: Int,
  onChange: (Int) -> Unit,
  fieldName: String,
  title: String,
  numberConstraint: NumberConstraint,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  inputField(
    value = value.toString(),
    onChange = { onChange.invoke(numberConstraint.constraint(it.parseInt())) },
    fieldName = fieldName,
    title = title,
  ) {
    attrs {
      type = InputType.number
    }

    configure(numberConstraint)
    config?.invoke(this)
  }
}

/**
 * Input for a double value
 */
fun RBuilder.doubleInput(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<Double>,
  fieldName: String,
  title: String,
  numberOfDecimals: Int = 2,
  placeholder: String? = null,
  numberConstraint: NumberConstraint,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  inputField(
    value = valueAndSetter.value.format(numberOfDecimals, false, I18nConfiguration.US),
    onChange = valueAndSetter.asOnChangeForDouble(numberConstraint),
    fieldName = fieldName,
    title = title,
    placeholder = placeholder,
  ) {
    attrs {
      type = InputType.number
    }

    configure(numberConstraint)
    config?.invoke(this)
  }
}

/**
 * Creates a password field backed by a [StateInstance]
 */
fun RBuilder.passwordField(
  valueAndSetter: StateInstance<String>,
  fieldName: String,
  title: String,
  placeholder: String? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  inputField(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    placeholder = placeholder,
    config = {
      attrs {
        type = InputType.password
      }
      config?.invoke(this)
    })
}

/**
 * Creates the input field
 */
fun RBuilder.inputField(
  /**
   * The current value
   */
  value: String,
  /**
   * Is called on change
   */
  onChange: OnChange<String>,
  /**
   * The field name
   */
  fieldName: String,
  /**
   * The title
   */
  title: String,

  /**
   * The (optional) placeholder.
   * If no placeholder is present, the title is used instead
   */
  placeholder: String? = null,

  /**
   * The classes for the input field
   */
  classes: String = "form-control",

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)?,

  ): Unit = child(inputField) {

  //ensureSameCallback(onChange, fieldName)

  attrs {
    require(value != undefined) {
      "Value must not be undefined but was <$value>"
    }

    this.value = value
    this.onChange = onChange
    this.title = title
    this.fieldName = fieldName
    this.placeholder = placeholder
    this.classes = classes
    this.config = config as ((RDOMBuilder<*>) -> Unit)?
  }
}

/**
 * an input field
 */
val inputField: FC<InputFieldProps> = fc("inputField") { props ->

  input(name = props.fieldName, classes = props.classes) {

    attrs {
      placeholder = props.placeholder ?: props.title
      title = props.title

      //Always assign the value in element
      value = props.value

      val onChange = props.onChange
      onChangeFunction = {
        val updatedValue = (it.target as HTMLInputElement).value

        //Notify the change listener - this *may* result in model changes, but sometimes does *not*
        onChange(updatedValue)
      }
    }

    props.config?.invoke(this)
  }

}

/**
 * Creates an input area
 */
fun RBuilder.inputArea(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String>,

  fieldName: String,
  title: String,

  /**
   * The (optional) placeholder - if no placeholder is provided, the title is used
   */
  placeHolder: String? = null,

  /**
   * The classes for the input field
   */
  classes: String = "form-control",

  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,

  ) {
  child(inputArea) {
    attrs {
      this.value = valueAndSetter.value
      this.onChange = { valueAndSetter.setter.invoke(it) }
      this.title = title
      this.fieldName = fieldName
      this.placeholder = placeHolder
      this.classes = classes
      this.config = config as ((RDOMBuilder<*>) -> Unit)?
    }
  }
}

/**
 * Creates a text area
 */
val inputArea: FC<InputFieldProps> = fc("inputArea") { props ->
  textarea(classes = "form-control") {
    attrs {
      name = props.fieldName

      placeholder = props.placeholder ?: props.title
      title = props.title
      value = props.value

      val onChange = props.onChange
      onChangeFunction = { event ->
        val element = event.target as HTMLTextAreaElement
        onChange(element.value)
      }

      onKeyPress = { keyboardEvent ->
        keyboardEvent.stopPropagation()
      }
    }

    props.config?.invoke(this)
  }
}

/**
 * Properties for text input
 */
external interface InputFieldProps : Props {
  /**
   * The (optional) classes
   */
  var classes: String?

  /**
   * The initial value for the field
   */
  var value: String

  /**
   * The name of the field
   */
  var fieldName: String

  /**
   * The setter that is called when the value has changed
   */
  var onChange: OnChange<String>

  /**
   * The title
   */
  var title: String

  /**
   * The (optional) placeholder.
   * If no placeholder is set, the title is used instead
   */
  var placeholder: String?

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<*>) -> Unit)?
}


/**
 * Creates the input field
 */
fun RBuilder.nullableInputField(
  /**
   * The current value
   */
  value: String?,
  /**
   * Is called on change
   */
  onChange: OnChange<String?>,
  /**
   * The field name
   */
  fieldName: String,
  /**
   * The title
   */
  title: String,

  /**
   * The (optional) placeholder.
   * If no placeholder is present, the title is used instead
   */
  placeholder: String?,

  /**
   * The classes for the input field
   */
  classes: String = "form-control",

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)?,

  ): Unit = child(nullableInputField) {

  ensureSameCallback(onChange, fieldName)

  attrs {
    require(value != undefined) {
      "Value must not be undefined but was <$value>"
    }

    this.value = value
    this.onChange = onChange
    this.title = title
    this.fieldName = fieldName
    this.placeholder = placeholder
    this.classes = classes
    this.config = config as ((RDOMBuilder<*>) -> Unit)?
  }
}

/**
 * an input field
 */
val nullableInputField: FC<NullableTextInputProps> = fc("nullableInputField") { props ->

  input(name = props.fieldName, classes = props.classes) {

    attrs {
      placeholder = props.placeholder ?: props.title
      title = props.title

      //Always assign the value in element
      props.value?.let {
        value = it
      }

      val onChange = props.onChange
      onChangeFunction = {
        val updatedValue = (it.target as HTMLInputElement).value

        //Notify the change listener - this *may* result in model changes, but sometimes does *not*
        onChange(updatedValue)
      }
    }

    props.config?.invoke(this)
  }

}

fun RBuilder.nullableInputArea(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String?>,

  fieldName: String,
  title: String,

  /**
   * The (optional) placeholder - if no placeholder is provided, the title is used
   */
  placeHolder: String? = null,

  /**
   * The classes for the input field
   */
  classes: String = "form-control",

  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,

  ) {
  child(nullableInputArea) {
    attrs {
      this.value = valueAndSetter.value
      this.onChange = { valueAndSetter.setter.invoke(it) }
      this.title = title
      this.fieldName = fieldName
      this.placeholder = placeHolder
      this.classes = classes
      this.config = config as ((RDOMBuilder<*>) -> Unit)?
    }
  }
}

/**
 * Creates a text area
 */
val nullableInputArea: FC<NullableTextInputProps> = fc("nullableInputArea") { props ->

  textarea(classes = props.classes) {

    attrs {
      name = props.fieldName

      placeholder = props.placeholder ?: props.title
      title = props.title

      //Always assign the value in element
      props.value?.let {
        value = it
      }

      val onChange = props.onChange
      onChangeFunction = {
        val updatedValue = (it.target as HTMLInputElement).value

        //Notify the change listener - this *may* result in model changes, but sometimes does *not*
        onChange(updatedValue)
      }
    }

    props.config?.invoke(this)
  }

}

/**
 * Properties for nullable text input
 */
external interface NullableTextInputProps : Props {
  /**
   * The (optional) classes
   */
  var classes: String?

  /**
   * The initial value for the field
   */
  var value: String?

  /**
   * The name of the field
   */
  var fieldName: String

  /**
   * The setter that is called when the value has changed
   */
  var onChange: OnChange<String?>

  /**
   * The title
   */
  var title: String

  /**
   * The (optional) placeholder.
   * If no placeholder is set, the title is used instead
   */
  var placeholder: String?

  /**
   * The configuration for the input field
   */
  var config: ((RDOMBuilder<*>) -> Unit)?
}


/**
 * Configure the input for the given number constraint
 */
fun RDOMBuilder<INPUT>.configure(numberConstraint: NumberConstraint) {
  when (numberConstraint) {
    ZeroOrPositive -> {
      zeroOrPositiveValues()
    }

    is CustomIntegerConstraint -> {
      attrs {
        min = "${numberConstraint.lowerConstraint}"
        max = "${numberConstraint.upperConstraint}"
      }
    }

    else -> {}
  }
}

/**
 * Sets the min value to "0"
 */
fun INPUT.zeroOrPositiveValues() {
  min = "0"
}

/**
 * Only support positive values
 */
fun RDOMBuilder<INPUT>.zeroOrPositiveValues() {
  attrs {
    zeroOrPositiveValues()
  }
}

