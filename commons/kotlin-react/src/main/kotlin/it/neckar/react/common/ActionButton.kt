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
   * The icon for the button
   */
  icon: String,
  /**
   * The label on the button
   */
  text: String? = null,

  /**
   * The css classes
   */
  classes: String = "btn btn-primary",

  /**
   * If set to true, the button will be disabled and a spinner icon is shown instead of the provided icon
   */
  busy: Boolean = false,
  /**
   * The action that is called
   */
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
    icon(FontAwesomeIcons.signOut)
  }

  fun RBuilder.faBug() {
    icon(FontAwesomeIcons.bug)
  }

  fun RBuilder.faHome() {
    icon(FontAwesomeIcons.home)
  }

  fun RBuilder.faEdit() {
    icon(FontAwesomeIcons.edit)
  }

  fun RBuilder.faSave() {
    i(classes = FontAwesomeIcons.save) {}
  }

  fun RBuilder.faAdd() {
    i(classes = FontAwesomeIcons.add) {}
  }

  fun RBuilder.faPhone() {
    i(classes = FontAwesomeIcons.phone) {}
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

  fun RBuilder.faArrowRight() {
    i(classes = FontAwesomeIcons.arrowRight) {}
  }

  fun RBuilder.faArrowLeft() {
    i(classes = FontAwesomeIcons.arrowLeft) {}
  }

  fun RBuilder.faUser() {
    i(classes = FontAwesomeIcons.user) {}
  }

  fun RBuilder.faAddUser() {
    i(classes = FontAwesomeIcons.addUser) {}
  }

  fun RBuilder.faInformation() {
    i(classes = FontAwesomeIcons.information) {}
  }

  fun RBuilder.faInQuestionCircle() {
    i(classes = FontAwesomeIcons.questionCircle) {}
  }

  fun RBuilder.faInExclamationCircle() {
    i(classes = FontAwesomeIcons.exclamationCircle) {}
  }

  fun RBuilder.faLockClosed() {
    i(classes = FontAwesomeIcons.lock) {}
  }

  fun RBuilder.faLockOpen() {
    i(classes = FontAwesomeIcons.lockOpen) {}
  }

  fun RBuilder.faEye() {
    i(classes = FontAwesomeIcons.eye) {}
  }

  fun RBuilder.faEyeSlash() {
    i(classes = FontAwesomeIcons.eyeSlash) {}
  }

  fun RBuilder.faCircleCheck() {
    i(classes = FontAwesomeIcons.circleCheck) {}
  }

  fun RBuilder.faMinus() {
    i(classes = FontAwesomeIcons.minus) {}
  }

  /**
   * Creates a new icon using "i" with the given icon class
   */
  fun RBuilder.icon(iconClass: String) {
    i(classes = iconClass) {}
  }
}

/**
 * Contains font awesome icons
 *
 * https://fontawesome.com/search?m=free&o=r
 */
object FontAwesomeIcons {
  const val home: String = "fa-solid fa-house"
  const val information: String = "fas fa-bars"
  const val login: String = "fa fa-eye"
  const val signIn: String = "fa fa-sign-in-alt"
  const val signOut: String = "fa fa-sign-out-alt"
  const val bug: String = "fa-solid fa-bug"
  const val copy: String = "fa fa-copy"
  const val open: String = "fa fa-folder-open"
  const val eye: String = "fas fa-eye"
  const val eyeSlash: String = "fa-regular fa-eye-slash"
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
  const val location: String = "fa-solid fa-location-dot"
  const val person: String = "fa-solid fa-person"
  const val questionCircle: String = "fa-solid fa-circle-question"
  const val exclamationCircle: String = "fa-solid fa-circle-exclamation"
  const val lockOpen: String = "fa-solid fa-lock-open"
  const val lock: String = "fa-solid fa-lock"
  const val circleCheck: String = "fa-regular fa-circle-check"
  const val minus: String = "fa-solid fa-minus"

  const val horizontalArrows: String = "fas fa-arrows-alt-h"
  const val verticalArrows: String = "fas fa-arrows-alt-v"
  const val arrowRight: String = "fa-solid fa-arrow-right"
  const val arrowLeft: String = "fa-solid fa-arrow-left"
}
