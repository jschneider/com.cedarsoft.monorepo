package it.neckar.react.common.form

import it.neckar.react.common.*
import it.neckar.react.common.FontAwesome.faSave
import kotlinx.html.js.onClickFunction
import kotlinx.html.role
import org.w3c.dom.events.Event
import react.*
import react.dom.*

/**
 * Adds cancel/ok buttons for a form
 */
fun RBuilder.formButtons(
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit
): Unit = child(formButtons) {
  attrs {
    this.cancelAction = cancelAction
    this.okAction = okAction
  }
}

val formButtons: FunctionComponent<FormButtonsProps> = fc("formButtons") { props ->
  div(classes = "btn-group mt-3 ") {
    attrs {
      role = "group"
      ariaLabel = "Form Buttons"
    }

    button(classes = "btn btn-secondary") {
      +"Abbrechen"

      attrs {
        onClickFunction = props.cancelAction
      }
    }

    button(classes = "btn btn-primary") {
      faSave()
      span {
        +" Speichern"
      }

      attrs {
        onClickFunction = props.okAction
      }
    }
  }
}

external interface FormButtonsProps : Props {
  var cancelAction: (Event) -> Unit
  var okAction: (Event) -> Unit
}
