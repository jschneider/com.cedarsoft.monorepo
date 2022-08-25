package it.neckar.react.common.form

import com.cedarsoft.common.kotlin.lang.nullIfBlank
import com.cedarsoft.formatting.format
import com.cedarsoft.i18n.I18nConfiguration
import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.InputType
import react.*
import react.dom.*

/**
 * Creates a floating input field
 */
fun RBuilder.floatingInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String>,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus = Editable,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(floatingInputField) {
  attrs {
    this.value = valueAndSetter.value
    this.onChange = useCallback(valueAndSetter.setter) { valueAndSetter.setter(it) }
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = config
  }
}

fun RBuilder.floatingReadOnlyInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  value: String,

  fieldName: String,
  title: String,

  editableStatus: EditableStatus,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(floatingInputField) {
  attrs {
    this.value = value
    this.onChange = useCallback { throw UnsupportedOperationException("can not change value for read only input field") }
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = {
      it.attrs {
        readonly = true
      }
      config?.invoke(it)
    }
  }
}

fun RBuilder.floatingIntInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<Int>,

  fieldName: String,
  title: String,
  numberConstraint: NumberConstraint = Unconstraint,

  editableStatus: EditableStatus,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(floatingInputField) {
  attrs {
    this.value = valueAndSetter.value.toString()
    this.onChange = valueAndSetter.asOnChangeForInt(numberConstraint)
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = {
      it.attrs {
        type = InputType.number
      }

      it.configure(numberConstraint)
      config?.invoke(it)
    }
  }
}

fun RBuilder.floatingIntInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  value: Int,

  onChange: (Int) -> Unit,

  fieldName: String,
  title: String,
  numberConstraint: NumberConstraint = Unconstraint,

  editableStatus: EditableStatus,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(floatingInputField) {
  attrs {
    this.value = value.toString()
    this.onChange = useCallback(onChange) { s ->
      s.toIntOrNull()?.let { parsedInt ->
        onChange(numberConstraint.constraint(parsedInt))
      }
    }
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = {
      it.attrs {
        type = InputType.number
      }
      it.configure(numberConstraint)
      config?.invoke(it)
    }
  }
}

fun RBuilder.nullableFloatingIntInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<Int?>,

  fieldName: String,
  title: String,
  numberConstraint: NumberConstraint = Unconstraint,

  editableStatus: EditableStatus,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(nullableFloatingInputField) {
  attrs {
    this.value = valueAndSetter.value.toString().nullIfBlank()
    this.onChange = valueAndSetter.asOnChangeForInt(numberConstraint)
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = {
      it.attrs {
        type = InputType.number
      }

      it.configure(numberConstraint)
      config?.invoke(it)
    }
  }
}

fun RBuilder.floatingDoubleInputField(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<Double>,

  fieldName: String,
  title: String,

  numberOfDecimals: Int = 2,

  numberConstraint: NumberConstraint = Unconstraint,

  editableStatus: EditableStatus,

  divConfig: ((RDOMBuilder<DIV>).() -> Unit)? = null,
  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ) {
  floatingDoubleInputField(
    value = valueAndSetter.value,
    onChange = valueAndSetter.asOnChangeForDouble(numberConstraint),//useCallback(valueAndSetter.setter) { valueAndSetter.setter(it) },
    fieldName = fieldName,
    title = title,
    numberOfDecimals = numberOfDecimals,
    editableStatus = editableStatus,
    divConfig = divConfig,
  ) {
    config?.let { it() }
    configure(numberConstraint)
  }
}

fun RBuilder.floatingDoubleInputField(
  value: Double,

  onChange: (String) -> Unit,

  fieldName: String,
  title: String,

  numberOfDecimals: Int = 2,

  editableStatus: EditableStatus,

  divConfig: (RDOMBuilder<DIV>.() -> Unit)?,
  config: (RDOMBuilder<INPUT>.() -> Unit)?,

  ): Unit = child(floatingInputField) {

  attrs {
    this.value = value.format(numberOfDecimals, 0, 1, false, I18nConfiguration.US) //always format with US, since the input field expects "." as separator
    this.onChange = useCallback(onChange) {
      onChange(it)
    }
    this.fieldName = fieldName
    this.title = title
    this.editableStatus = editableStatus
    this.divConfig = divConfig
    this.config = {
      it.attrs {
        type = InputType.number
      }
      config?.invoke(it)
    }
  }
}


val floatingInputField: FC<FloatingInputFieldProps> = fc("floatingInputField") { props ->

  div("form-floating") {
    attrs {
      props.divConfig?.invoke(this@div)
    }

    inputFieldAndLabel(
      value = props.value,
      onChange = props.onChange,
      fieldName = props.fieldName,
      title = props.title,
      editableStatus = props.editableStatus,
      config = props.config,
    )
  }

}

val nullableFloatingInputField: FC<NullableFloatingInputFieldProps> = fc("nullableFloatingInputField") { props ->

  div("form-floating") {
    attrs {
      props.divConfig?.invoke(this@div)
    }

    nullableInputFieldAndLabel(
      value = props.value,
      onChange = props.onChange,
      fieldName = props.fieldName,
      title = props.title,
      editableStatus = props.editableStatus,
      config = props.config,
    )
  }

}


external interface FloatingInputFieldProps : Props {
  /**
   * The unique ID that is used to connect input field and label
   */
  var id: String

  /**
   * The current value
   */
  var value: String

  /**
   * Is called on change
   */
  var onChange: OnChange<String>

  /**
   * The name of the field
   */
  var fieldName: String

  /**
   * The title that is shown to the user
   */
  var title: String

  var editableStatus: EditableStatus

  /**
   * Config for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?

  /**
   * Config for the div
   */
  var divConfig: ((RDOMBuilder<DIV>) -> Unit)?
}

external interface NullableFloatingInputFieldProps : Props {
  /**
   * The unique ID that is used to connect input field and label
   */
  var id: String

  /**
   * The current value
   */
  var value: String?

  /**
   * Is called on change
   */
  var onChange: OnChange<String?>

  /**
   * The name of the field
   */
  var fieldName: String

  /**
   * The title that is shown to the user
   */
  var title: String

  var editableStatus: EditableStatus

  /**
   * Config for the input field
   */
  var config: ((RDOMBuilder<INPUT>) -> Unit)?

  /**
   * Config for the div
   */
  var divConfig: ((RDOMBuilder<DIV>) -> Unit)?
}
