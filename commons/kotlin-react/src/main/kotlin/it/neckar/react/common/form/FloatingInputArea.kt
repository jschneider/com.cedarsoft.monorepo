package it.neckar.react.common.form

import it.neckar.react.common.*
import it.neckar.react.common.form.EditableStatus.*
import kotlinx.html.TEXTAREA
import kotlinx.html.id
import react.*
import react.dom.*

/**
 * Creates a floating input area
 */
fun RBuilder.floatingInputArea(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String>,

  fieldName: String,
  title: String,
  placeHolder: String? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,

  ): Unit = child(floatingInputArea) {
  attrs {
    this.valueAndSetter = valueAndSetter
    this.fieldName = fieldName
    this.title = title
    this.placeHolder = placeHolder
    this.editableStatus = editableStatus
    this.config = config
  }
}

val floatingInputArea: FC<FloatingInputAreaProps> = fc("floatingInputArea") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  div("form-floating") {
    inputArea(
      props.valueAndSetter,
      fieldName = props.fieldName,
      title = props.title,
    ) {
      attrs {
        id = uniqueId
        readonly = props.editableStatus == ReadOnly

        addClasses("form-control height-100px")

        //Execute the provided config if there is one
        props.config?.let {
          it(this@inputArea)
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
}


fun RBuilder.nullableFloatingInputArea(
  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  valueAndSetter: StateInstance<String?>,

  fieldName: String,
  title: String,
  placeHolder: String? = null,

  editableStatus: EditableStatus = Editable,

  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,

  ): Unit = child(nullableFloatingInputArea) {
  attrs {
    this.valueAndSetter = valueAndSetter
    this.fieldName = fieldName
    this.title = title
    this.placeHolder = placeHolder
    this.editableStatus = editableStatus
    this.config = config
  }
}

val nullableFloatingInputArea: FC<NullableFloatingInputAreaProps> = fc("nullableFloatingInputArea") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  div("form-floating") {
    nullableInputArea(
      valueAndSetter = props.valueAndSetter,
      fieldName = props.fieldName,
      title = props.title,
      classes = "form-control height-100px",
    ) {
      attrs {
        id = uniqueId
        readonly = props.editableStatus == ReadOnly

        //Execute the provided config if there is one
        props.config?.let {
          it(this@nullableInputArea)
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
}


external interface FloatingInputAreaProps : Props {
  /**
   * The unique ID that is used to connect input area and label
   */
  var id: String

  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  var valueAndSetter: StateInstance<String>

  /**
   * The name of the text area
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

  var editableStatus: EditableStatus

  var config: ((RDOMBuilder<TEXTAREA>) -> Unit)?
}

external interface NullableFloatingInputAreaProps : Props {
  /**
   * The unique ID that is used to connect input area and label
   */
  var id: String

  /**
   * The value and setter for the value.
   * Must be created with a useState hook
   */
  var valueAndSetter: StateInstance<String?>

  /**
   * The name of the text area
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

  var editableStatus: EditableStatus

  var config: ((RDOMBuilder<TEXTAREA>) -> Unit)?
}
