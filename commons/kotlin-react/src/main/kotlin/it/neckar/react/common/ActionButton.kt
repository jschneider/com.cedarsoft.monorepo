package it.neckar.react.common

import kotlinx.coroutines.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * Creates an action button
 */
fun RBuilder.actionButton(
  /**
   *
   */
  icon: String,

  text: String? = null,

  classes: String = "btn btn-primary",

  /**
   * If set to true, the button will be disabled and a spinner icon is shown instead of the provided icon
   */
  busy: Boolean = false,

  action: suspend () -> Unit,

  ): Unit = child(actionButton) {
  attrs {
    this.icon = icon
    this.classes = classes
    this.busy = busy
    this.text = text
    this.action = action
  }
}

val actionButton: FC<ActionButtonProps> = fc("actionButton") { props ->

  val busy = props.busy


  button(classes = props.classes) {
    i(if (!busy) props.icon else "spinner-border spinner-border-sm") { }

    attrs {
      disabled = busy
      onClickFunction = useCallback(props.action) {
        AppScope.launch {
          props.action()
        }
      }
    }

    props.text?.let {
      b("ms-2") {
        +it
      }
    }
  }

}

external interface ActionButtonProps : Props {
  var classes: String

  var icon: String

  var busy: Boolean

  var text: String?

  var action: suspend () -> Unit
}


fun RBuilder.editButton(action: suspend () -> Unit) {
  actionButton(icon = FontAwesomeIcons.edit, classes = "btn btn-link btn-sm", action = action)
}

fun RBuilder.addButton(action: suspend () -> Unit) {
  actionButton(icon = FontAwesomeIcons.add, classes = "btn btn-link btn-sm", action = action)
}

fun RBuilder.copyButton(action: suspend () -> Unit) {
  actionButton(icon = FontAwesomeIcons.copy, classes = "btn btn-link btn-sm", action = action)
}

fun RBuilder.deleteButton(action: suspend () -> Unit) {
  actionButton(icon = FontAwesomeIcons.trash, classes = "btn btn-link btn-sm", action = action)
}

object FontAwesome {
  /**
   * Adds a sign-out span
   */
  fun RBuilder.faSignOut() {
    i(classes = FontAwesomeIcons.signOut) {}
  }

  fun RBuilder.faSave() {
    i(classes = FontAwesomeIcons.save) {}
  }

  fun RBuilder.faEdit() {
    i(classes = FontAwesomeIcons.edit) {}
  }

  fun RBuilder.faAdd() {
    i(classes = FontAwesomeIcons.add) {}
  }

  fun RBuilder.faReload() {
    i(classes = FontAwesomeIcons.sync) {}
  }

  fun RBuilder.faGift() {
    i(classes = FontAwesomeIcons.gift) {}
  }

  fun RBuilder.faHorizontalArrows() {
    i(classes = FontAwesomeIcons.horizontalArrows) {}
  }

  fun RBuilder.faVerticalArrows() {
    i(classes = FontAwesomeIcons.verticalArrows) {}
  }

  fun RBuilder.faUser() {
    i(classes = FontAwesomeIcons.user) {}
  }

  fun RBuilder.faAddUser() {
    i(classes = FontAwesomeIcons.addUser) {}
  }
}

/**
 * Contains font awesome icons
 */
object FontAwesomeIcons {
  //const val home: String = "fa-solid fa-house" //TODO update bootstrap
  const val information: String = "fas fa-bars"
  const val login: String = "fa fa-eye"
  const val signIn: String = "fa fa-sign-in-alt"
  const val signOut: String = "fa fa-sign-out-alt"
  const val copy: String = "fa fa-copy"
  const val open: String = "fa fa-folder-open"
  const val eye: String = "fas fa-eye"
  const val save: String = "fa fa-save"
  const val download: String = "fa fa-download"
  const val busy: String = "fa fa-spinner fa-spin"
  const val add: String = "fas fa-plus"
  const val solarPanel: String = "fas fa-solar-panel"
  const val map: String = "fas fa-map-marked-alt"
  const val trash: String = "fas fa-trash"
  const val edit: String = "fas fa-pen"
  const val sync: String = "fas fa-sync-alt"
  const val phone: String = "fa fa-phone"
  const val gift: String = "fas fa-gift"
  const val user: String = "fas fa-user"
  const val addUser: String = "fa fa-user-plus"

  const val horizontalArrows: String = "fas fa-arrows-alt-h"
  const val verticalArrows: String = "fas fa-arrows-alt-v"
}
