package it.neckar.react.common

import kotlinx.html.js.onClickFunction
import kotlinx.html.title
import react.*
import react.dom.*

/**
 *
 */
fun RBuilder.linkAction(
  icon: String? = null,
  label: String? = null,
  title: String? = null,
  action: () -> Unit
): Unit = child(linkAction) {
  attrs {
    this.icon = icon
    this.label = label
    this.action = action
    this.title = title
  }
}

val linkAction: FunctionComponent<LinkActionProps> = fc("linkAction") { props ->
  a("#") {
    props.label?.let {
      +it
    }

    attrs {
      onClickFunction = useCallback(props.action) {
        //do *not* follow the link
        it.preventDefault()
        props.action()
      }

      props.title?.let {
        title = it
      }
    }
  }
}

external interface LinkActionProps : Props {
  var icon: String?
  var label: String?
  var title: String?

  var action: () -> Unit
}
