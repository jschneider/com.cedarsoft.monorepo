package it.neckar.react.common.form

import it.neckar.react.common.*
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.id
import react.*
import react.dom.*

fun RBuilder.nullableInputFieldAndLabel(
  /**
   * The current value
   */
  value: String?,

  /**
   * Is called on change
   */
  onChange: OnChange<String?>,

  fieldName: String,
  title: String,
  placeholder: String?=null,

  editableStatus: EditableStatus,

  config: (RDOMBuilder<INPUT>.() -> Unit)? = null,

  ): Unit = child(nullableInputFieldAndLabel) {
  attrs {
    this.value = value
    this.onChange = onChange
    this.fieldName = fieldName
    this.title = title
    this.placeholder = placeholder
    this.editableStatus = editableStatus
    this.config = config
  }
}

val nullableInputFieldAndLabel: FC<NullableInputFieldAndLabelProps> = fc("nullableInputFieldAndLabel") { props ->

  val uniqueId = uniqueIdMemo(props.fieldName)


  nullableInputField(
    value = props.value,
    onChange = props.onChange,
    fieldName = props.fieldName,
    title = props.title,
    placeholder = props.placeholder,
    classes = "form-control",
  ) {
    attrs {
      id = uniqueId
      type = InputType.text

      readonly = props.editableStatus == EditableStatus.ReadOnly

      //Execute the provided config if there is one
      props.config?.let {
        it(this@nullableInputField)
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

external interface NullableInputFieldAndLabelProps : Props {
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

  /**
   * The (optional) placeholder - if no placeholder is provided, the title is used
   */
  var placeholder: String?

  var editableStatus: EditableStatus

  var config: ((RDOMBuilder<INPUT>) -> Unit)?
}
