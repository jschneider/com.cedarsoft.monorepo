package it.neckar.react.common.form

import it.neckar.react.common.*
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
  config: (RDOMBuilder<TEXTAREA>.() -> Unit)? = null,
): Unit = child(floatingInputArea) {
  attrs {
    this.valueAndSetter = valueAndSetter
    this.fieldName = fieldName
    this.title = title
    this.placeHolder = placeHolder
    this.config = config
  }
}

val floatingInputArea: FunctionComponent<FloatingInputAreaProps> = fc("floatingInputArea") { props ->
  val uniqueId = uniqueIdMemo(props.fieldName)

  div("form-floating") {
    inputArea(
      props.valueAndSetter,
      fieldName = props.fieldName,
      title = props.title,
    ) {
      attrs {
        id = uniqueId
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

  var config: ((RDOMBuilder<TEXTAREA>) -> Unit)?
}

