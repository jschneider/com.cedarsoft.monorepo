package it.neckar.react.common

import kotlinx.html.BUTTON
import kotlinx.html.ButtonType
import kotlinx.html.id
import react.*
import react.dom.*


/**
 * Creates a button that shows a modal window
 */
fun RBuilder.modalButton(
  idOfModal: String,
  classes: String? = null,
  block: RDOMBuilder<BUTTON>.() -> Unit
): Unit = child(modalButton) {
  attrs {
    this.id = idOfModal
    this.classes = classes
    this.block = block
  }
}

val modalButton: FunctionComponent<ModalButtonProps> = fc("modalButton") { props ->
  button(classes = props.classes) {
    props.block(this)

    attrs["data-bs-toggle"] = "modal"
    attrs["data-bs-target"] = "#${props.id}"
  }
}

external interface ModalButtonProps : Props {
  var id: String
  var classes: String?
  var block: (RDOMBuilder<BUTTON>) -> Unit
}


/**
 * Shows a modal dialog
 */
fun RBuilder.modal(
  id: String,
  handler: RElementBuilder<ModalProps>.() -> Unit
) {
  child(modal, handler = {
    this.attrs {
      this.id = id
    }

    handler()
  })
}

val modal: FunctionComponent<ModalProps> = fc("modal") { props ->
  div(classes = "modal fade") {
    attrs {
      id = props.id
    }

    if (props.staticBackdrop == true) {
      attrs["data-bs-backdrop"] = "static"
    }

    div(classes = "modal-dialog modal-dialog-scrollable ${props.size?.cssClass ?: ""}") {
      div(classes = "modal-content") {
        div(classes = "modal-header") {
          props.title?.let { title ->
            h5(classes = "modal-title") {
              +title
            }
          }

          button(classes = "btn-close") {
            attrs {
              type = ButtonType.button
            }

            attrs["data-bs-dismiss"] = "modal"
          }
        }

        div(classes = "modal-body") {
          props.children()
        }

        div(classes = "modal-footer") {
          button(classes = "btn btn-primary") {
            +"Schließen"

            attrs["data-bs-dismiss"] = "modal"
            attrs {
              type = ButtonType.button
            }
          }
        }
      }
    }
  }
}

external interface ModalProps : PropsWithChildren {
  var id: String
  var title: String?

  var size: ModalSize?
  var staticBackdrop: Boolean?
}

enum class ModalSize(val cssClass: String?) {
  Small("modal-sm"),
  Default(null),
  Large("modal-lg"),
  ExtraLarge("modal-xl")
}
