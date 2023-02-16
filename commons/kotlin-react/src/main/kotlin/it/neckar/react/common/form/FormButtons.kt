package it.neckar.react.common.form

import it.neckar.commons.kotlin.js.safeGet
import it.neckar.react.common.*
import it.neckar.react.common.form.IconAlignment.*
import kotlinx.html.BUTTON
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

  cancelConfig: ((RDOMBuilder<BUTTON>) -> Unit)? = null,
  okConfig: ((RDOMBuilder<BUTTON>) -> Unit)? = null,
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit,
): Unit = child(FormButtons) {
  attrs {
    this.cancelIcon = cancelIcon
    this.cancelText = cancelText
    this.okIcon = okIcon
    this.okText = okText

    this.cancelConfig = cancelConfig
    this.okConfig = okConfig
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

  cancelConfig: ((RDOMBuilder<BUTTON>) -> Unit)? = null,
  okConfig: ((RDOMBuilder<BUTTON>) -> Unit)? = null,
  cancelAction: (Event) -> Unit,
  okAction: (Event) -> Unit,
): Unit = child(FormButtons) {
  attrs {
    this.cancelIcon = cancelIcon
    this.cancelText = cancelText
    this.okIcon = okIcon
    this.okText = okText

    this.cancelConfig = cancelConfig
    this.okConfig = okConfig
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

val FormButtons: FC<FormButtonsProps> = fc("FormButtons") { props ->
  val cancelIcon = props::cancelIcon.safeGet()
  val cancelText = props::cancelText.safeGet()
  val okIcon = props::okIcon.safeGet()
  val okText = props::okText.safeGet()

  val cancelConfig = props::cancelConfig.safeGet()
  val okConfig = props::okConfig.safeGet()
  val cancelAction = props::cancelAction.safeGet()
  val okAction = props::okAction.safeGet()


  div(classes = "btn-group mt-3 ") {
    attrs {
      role = "group"
      ariaLabel = "Form Buttons"
    }

    button(classes = "btn btn-secondary") {
      cancelIcon?.let {
        if (it.alignment == Left) {
          i("${it.icon} me-2") {}
        }
      }

      +cancelText

      cancelIcon?.let {
        if (it.alignment == Right) {
          i("${it.icon} ms-2") {}
        }
      }

      attrs {
        onClickFunction = cancelAction
      }

      cancelConfig?.invoke(this)
    }

    button(classes = "btn btn-primary") {
      okIcon?.let {
        if (it.alignment == Left) {
          i("${it.icon} me-2") {}
        }
      }

      b {
        +okText
      }

      okIcon?.let {
        if (it.alignment == Right) {
          i("${it.icon} ms-2") {}
        }
      }

      attrs {
        onClickFunction = okAction
      }

      okConfig?.invoke(this)
    }
  }

}


external interface FormButtonsProps : Props {
  var cancelIcon: ButtonIcon?
  var cancelText: String
  var okIcon: ButtonIcon?
  var okText: String

  var cancelConfig: ((RDOMBuilder<BUTTON>) -> Unit)?
  var okConfig: ((RDOMBuilder<BUTTON>) -> Unit)?
  var cancelAction: (Event) -> Unit
  var okAction: (Event) -> Unit
}
