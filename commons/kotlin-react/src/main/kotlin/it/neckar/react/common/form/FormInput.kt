package it.neckar.react.common.form

import com.cedarsoft.common.kotlin.lang.parseInt
import com.cedarsoft.formatting.format
import com.cedarsoft.i18n.I18nConfiguration
import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.TEXTAREA
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
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

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(checkbox) {
  attrs {
    this.value = value
    this.onChange = onChange
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.config = config
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

  editableStatus: EditableStatus,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  checkbox(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    editableStatus = editableStatus,
    config = config,
  )
}

val checkbox: FC<CheckboxProps> = fc("checkbox") { props ->
  val uniqueId = uniqueIdMemo("checkbox")

  div(classes = "form-check") {

    input(name = props.fieldName, classes = "form-check-input") {
      attrs {
        id = uniqueId

        placeholder = props.title
        title = props.title
        checked = props.value

        type = InputType.checkBox

        disabled = props.editableStatus == ReadOnly

        onChangeFunction = {
          val inputElement = it.target as HTMLInputElement
          props.onChange(inputElement.checked)
        }
      }

      props.config?.invoke(this)
    }

    label(classes = "form-check-label") {
      +props.title

      attrs {
        htmlForFixed = uniqueId
      }
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

  var config: ((RDOMBuilder<INPUT>) -> Unit)?
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
  placeHolder: String? = null,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ) {
  inputField(
    value = valueAndSetter.value,
    onChange = useCallback(valueAndSetter.setter) { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    placeHolder = placeHolder,
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
    onChange = useCallback(valueAndSetter.setter) { valueAndSetter.setter.invoke(it) },
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
    onChange = useCallback(onChange) {
      onChange.invoke(numberConstraint.constraint(it.parseInt()))
    },
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
  placeHolder: String? = null,
  numberConstraint: NumberConstraint,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  inputField(
    value = valueAndSetter.value.format(numberOfDecimals, false, I18nConfiguration.US),
    onChange = valueAndSetter.asOnChangeForDouble(numberConstraint),
    fieldName = fieldName,
    title = title,
    placeHolder = placeHolder,
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
  placeHolder: String? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,
) {
  inputField(
    value = valueAndSetter.value,
    onChange = { valueAndSetter.setter.invoke(it) },
    fieldName = fieldName,
    title = title,
    placeHolder = placeHolder,
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
  placeHolder: String? = null,

  /**
   * The classes for the input field
   */
  classes: String = "form-control",

  /**
   * Configuration for the input field
   */
  config: (RDOMBuilder<INPUT>.() -> Unit)?,

  ): Unit = child(inputField) {

  ensureSameCallback(onChange, fieldName)

  attrs {
    requireNotNull(value) {
      "Value must not be null but was <$value>"
    }
    require(value != undefined) {
      "Value must not be undefined but was <$value>"
    }

    this.value = value
    this.onChange = onChange
    this.title = title
    this.fieldName = fieldName
    this.placeHolder = placeHolder
    this.classes = classes
    this.config = config as ((RDOMBuilder<*>) -> Unit)?
  }
}

/**
 * an input field
 */
val inputField: FC<TextInputProps> = fc("inputField") { props ->
  input(name = props.fieldName, classes = props.classes) {
    //Contains the string from the model.
    //This state is used to identify changes to the model
    //The "valueInElement" is only updated if the model has changed
    val valueFromModel = useState(props.value)

    //Contains the string that is stored within the element
    //Is updated by user interactions
    //Is only updated from the model, if the model changes
    val valueInElement = useState(props.value)

    //verify if there is a new value in the model
    if (valueFromModel.value != props.value) {
      //Remember the change to be able to compare later
      valueFromModel.setter(props.value)

      //Update the value in the element!
      valueInElement.setter(props.value)
    }

    attrs {
      placeholder = props.placeHolder ?: props.title
      title = props.title

      //Always assign the value in element
      value = valueInElement.value

      val onChange = props.onChange
      onChangeFunction = useCallback(onChange) {
        val updatedValue = (it.target as HTMLInputElement).value

        //Automatically update the value in element
        valueInElement.setter(updatedValue)

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

  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,

  ) {
  child(inputArea) {
    attrs {
      this.value = valueAndSetter.value
      this.onChange = useCallback(valueAndSetter.setter) { valueAndSetter.setter.invoke(it) }
      this.title = title
      this.fieldName = fieldName
      this.placeHolder = placeHolder
      this.config = config as ((RDOMBuilder<*>) -> Unit)?
    }
  }
}

/**
 * Creates a text area
 */
val inputArea: FC<TextInputProps> = fc("inputArea") { props ->
  textarea(classes = "form-control") {
    attrs {
      name = props.fieldName

      placeholder = props.placeHolder ?: props.title
      title = props.title
      value = props.value

      val onChange = props.onChange
      onChangeFunction = useCallback(onChange) { event ->
        val element = event.target as HTMLTextAreaElement
        onChange(element.value)
      }

      onKeyDown = useCallback { keyboardEvent ->
        keyboardEvent.stopPropagation()
      }
    }

    props.config?.invoke(this)
  }
}

/**
 * Properties for text input
 */
external interface TextInputProps : Props {
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
  var placeHolder: String?

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

    else -> {}
  }
}

/**
 * Sets the min value to "0"
 */
inline fun INPUT.zeroOrPositiveValues() {
  min = "0"
}

/**
 * Only support positive values
 */
inline fun RDOMBuilder<INPUT>.zeroOrPositiveValues() {
  attrs {
    zeroOrPositiveValues()
  }
}

