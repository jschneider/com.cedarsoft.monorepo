package it.neckar.react.common

import kotlinx.html.CommonAttributeGroupFacade
import react.dom.*
import react.dom.events.*


/**
 * Adds the given function call on the given keyCheckFunction condition
 */
fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.onKeyEvent(
  keyEventLambda: (KeyboardEvent<*>) -> Boolean,
  functionToCall: () -> Unit,
) {
  attrs {
    onKeyDown = { keyboardEvent ->
      if (keyEventLambda(keyboardEvent)) {
        functionToCall()
      }
    }
  }
}

/**
 * Adds the given function call on the given key press
 */
fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.onKeyEvent(
  keyString: String,
  functionToCall: () -> Unit,
) {
  onKeyEvent({ keyboardEvent -> keyboardEvent.key == keyString }, functionToCall)
}

/**
 * Adds the given function call on enter key press
 */
fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.onEnter(
  functionToCall: () -> Unit,
) {
  onKeyEvent("Enter", functionToCall)
}
