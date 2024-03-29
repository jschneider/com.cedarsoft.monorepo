package it.neckar.react.common

import kotlinx.browser.window

/**
 * Let the user confirm - then executes the action
 */
fun confirm(message: String, action: () -> Unit) {
  if (window.confirm(message)) {
    action()
  }
}

suspend fun suspendConfirm(message: String, action: suspend () -> Unit) {
  if (window.confirm(message)) {
    action()
  }
}
