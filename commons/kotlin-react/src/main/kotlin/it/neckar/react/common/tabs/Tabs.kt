package it.neckar.react.common.tabs

import it.neckar.react.common.*
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.id
import kotlinx.html.role
import react.*
import react.dom.*

/**
 * Represents the ID for a tab
 */
value class TabId(val value: String) {
  init {
    require(value.isNotBlank()) { "Invalid value <$value>" }
    require(value.first().isLetter()) { "Value must start with letter <$value>" }
  }

  override fun toString(): String {
    return value
  }
}

/**
 * Creates a tab button
 */
@Deprecated("Use react router tabs instead")
fun RBuilder.tabButton(name: String, id: TabId, active: Boolean = false) {
  li("nav-item") {
    attrs {
      role = "presentation"
    }
    button(classes = "nav-link", type = ButtonType.button) {
      attrs {
        this.id = "${id}Tab"
        role = "tab"
        if (active) addClass("active")
      }
      attrs["data-bs-toggle"] = "tab"
      attrs["data-bs-target"] = "#$id"
      attrs["aria-controls"] = id
      if (active) attrs["aria-selected"] = "true"

      +name
    }
  }
}

/**
 * Creates the tab content (for a tab button) - to be used with JavaScript
 */
@Deprecated("Use react router tabs instead")
fun RBuilder.tabContent(id: TabId, active: Boolean = false, content: RDOMBuilder<DIV>.() -> Unit) {
  div("tab-pane fade") {
    attrs {
      this.id = id.value
      role = "tabpanel"
      attrs["aria-labelledby"] = "$id-tab"
      if (active) addClass("show active")
    }

    content(this@div)
  }
}

fun RBuilder.tabContent(active: Boolean = false, content: RDOMBuilder<DIV>.() -> Unit) {
  div("tab-pane fade") {
    attrs {
      role = "tabpanel"
      if (active) addClass("show active") //TODO necessary(?)
    }

    content(this@div)
  }
}
