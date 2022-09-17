package it.neckar.react.common.tabs

import it.neckar.react.common.*
import kotlinx.html.ButtonType
import kotlinx.html.id
import kotlinx.html.role
import react.*
import react.dom.*

/**
 * Button to switch between displaying [tabContent]
 * Use inside `ul("nav nav-tabs")`
 */
fun RBuilder.tabButton(name: String, id: String, active: Boolean = false) {

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
