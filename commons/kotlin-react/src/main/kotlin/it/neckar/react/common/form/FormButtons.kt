package it.neckar.react.common.form

import it.neckar.react.common.*
import it.neckar.react.common.form.IconAlignment.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.org.w3c.dom.events.Event
import kotlinx.html.role
import react.*
import react.dom.*

/**
 * Adds cancel/ok buttons for a form
 */
fun RBuilder.formButtons(
  cancelIcon: ButtonIcon? = null,
  cancelText: String = "Abbrechen",
  okIcon: ButtonIcon? = ButtonIcon(
    icon = FontAwesomeIcons.save,
    alignment = Left,
  ),
  okText: String = "Speichern",
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit,
): Unit = child(formButtons) {
  attrs {
    this.cancelIcon = cancelIcon
    this.cancelText = cancelText
    this.okIcon = okIcon
    this.okText = okText
    this.cancelAction = cancelAction
    this.okAction = okAction
  }
}

fun RBuilder.wizardFormButtons(
  cancelIcon: ButtonIcon? = ButtonIcon(
    icon = FontAwesomeIcons.arrowLeft,
    alignment = Left,
  ),
  cancelText: String = "Zurück",
  okText: String = "Weiter",
  okIcon: ButtonIcon? = ButtonIcon(
    icon = FontAwesomeIcons.arrowRight,
    alignment = Right,
  ),
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit,
): Unit = child(formButtons) {
  attrs {
    this.cancelIcon = cancelIcon
    this.cancelText = cancelText
    this.okIcon = okIcon
    this.okText = okText
    this.cancelAction = cancelAction
    this.okAction = okAction
  }
}

enum class IconAlignment {
  Left,
  Right,
}

data class ButtonIcon(
  val icon: String,
  val alignment: IconAlignment = Left,
)

val formButtons: FC<FormButtonsProps> = fc("formButtons") { props ->
  div(classes = "btn-group mt-3 ") {
    attrs {
      role = "group"
      ariaLabel = "Form Buttons"
    }

    button(classes = "btn btn-secondary") {
      props.cancelIcon?.let {
        if (it.alignment == Left) {
          i("${it.icon} me-2") {}
        }
      }

      +props.cancelText

      props.cancelIcon?.let {
        if (it.alignment == Right) {
          i("${it.icon} ms-2") {}
        }
      }

      attrs {
        onClickFunction = props.cancelAction
      }
    }

    button(classes = "btn btn-primary") {
      props.okIcon?.let {
        if (it.alignment == Left) {
          i("${it.icon} me-2") {}
        }
      }

      b {
        +props.okText
      }

      props.okIcon?.let {
        if (it.alignment == Right) {
          i("${it.icon} ms-2") {}
        }
      }

      attrs {
        onClickFunction = props.okAction
      }
    }
  }
}


external interface FormButtonsProps : Props {
  var cancelIcon: ButtonIcon?
  var cancelText: String
  var okIcon: ButtonIcon?
  var okText: String
  var cancelAction: (Event) -> Unit
  var okAction: (Event) -> Unit
}
