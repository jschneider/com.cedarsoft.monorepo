package it.neckar.react.common.tabs

import it.neckar.react.common.*
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.role
import react.*
import react.dom.*

/**
 * Displays [content] in a tab which can be toggled with a [tabButton]
 */
fun RBuilder.tabContent(id: String, active: Boolean = false, content: RDOMBuilder<DIV>.() -> Unit) {

  div("tab-pane fade") {
    attrs {
      this.id = id
      role = "tabpanel"
      attrs["aria-labelledby"] = "$id-tab"
      if (active) addClass("show active")
    }

    content(this@div)
  }

}
