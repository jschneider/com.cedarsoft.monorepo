package it.neckar.react.common.form

import it.neckar.react.common.*
import it.neckar.react.common.FontAwesome.faSave
import kotlinx.html.js.onClickFunction
import kotlinx.html.org.w3c.dom.events.Event
import kotlinx.html.role
import react.*
import react.dom.*

/**
 * Adds cancel/ok buttons for a form
 */
fun RBuilder.formButtons(
  cancelText: String = "Abbrechen",
  okText: String = "Speichern",
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit,
): Unit = child(formButtons) {
  attrs {
    this.cancelText = cancelText
    this.okText = okText
    this.cancelAction = cancelAction
    this.okAction = okAction
  }
}

val formButtons: FC<FormButtonsProps> = fc("formButtons") { props ->
  div(classes = "btn-group mt-3 ") {
    attrs {
      role = "group"
      ariaLabel = "Form Buttons"
    }

    button(classes = "btn btn-secondary") {
      +props.cancelText

      attrs {
        onClickFunction = props.cancelAction
      }
    }

    button(classes = "btn btn-primary") {
      faSave()
      span("ms-2") {
        +props.okText
      }

      attrs {
        onClickFunction = props.okAction
      }
    }
  }
}

external interface FormButtonsProps : Props {
  var cancelText: String
  var okText: String
  var cancelAction: (Event) -> Unit
  var okAction: (Event) -> Unit
}
