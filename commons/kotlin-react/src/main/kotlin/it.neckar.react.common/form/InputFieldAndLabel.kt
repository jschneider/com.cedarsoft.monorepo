package it.neckar.react.common.form

import it.neckar.react.common.*
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.id
import react.*
import react.dom.*

fun RBuilder.inputFieldAndLabel(
  /**
   * The current value
   */
  value: String,

  /**
   * Is called on change
   */
  onChange: OnChange<String>,

  fieldName: String,
  title: String,
  placeHolder: String? = null,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(inputFieldAndLabel) {
  attrs {
    this.value = value
    this.onChange = onChange
    this.fieldName = fieldName
    this.title = title
    this.placeHolder = placeHolder
    this.config = config
  }
}

val inputFieldAndLabel: FunctionComponent<InputFieldAndLabelProps> = fc("inputFieldAndLabel") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  inputField(
    value = props.value,
    onChange = props.onChange,
    fieldName = props.fieldName,
    title = props.title,
    classes = "form-control",
  ) {
    attrs {
      id = uniqueId
      type = InputType.text

      //Execute the provided config if there is one
      props.config?.let {
        it(this@inputField)
      }
    }
  }
  label("form-label") {
    +props.title

    attrs {
      htmlForFixed = uniqueId
    }
  }
}

external interface InputFieldAndLabelProps : Props {
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

  /**
   * The (optional) placeholder - if no placeholder is provided, the title is used
   */
  var placeHolder: String?

  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}
