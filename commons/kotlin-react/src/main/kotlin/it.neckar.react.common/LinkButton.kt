package it.neckar.react.common

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 *
 */
fun RBuilder.linkButton(
  icon: String? = null,
  label: String? = null,
  action: () -> Unit
): Unit = child(linkButton) {
  attrs {
    this.icon = icon
    this.label = label
    this.action = action
  }
}

val linkButton: FC<LinkButtonProps> = fc("linkButton") { props ->
  button(classes = "btn btn-link") {
    props.label?.let {
      +it
    }

    props.icon?.let {
      i(it) {
      }
    }

    attrs {
      onClickFunction = useCallback(props.action) {
        props.action()
      }
    }
  }
}

external interface LinkButtonProps : Props {
  var icon: String?
  var label: String?

  var action: () -> Unit

}
