package it.neckar.react.common

import react.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Shows a busy indicator if the property is false
 */
fun RBuilder.busyIfTrue(value: Boolean, handler: RElementBuilder<*>.(Boolean) -> Unit) {
  contract {
    callsInPlace(handler, InvocationKind.AT_MOST_ONCE)
  }

  return child(busyIfTrue) {
    attrs {
      this.busy = value
    }

    this.handler(value)
  }
}

val busyIfTrue: FunctionComponent<BusyIfFalseProps> = fc("busyIfTrue") { props ->
  if (props.busy) {
    busyIndicator()
  } else {
    props.children()
  }
}

external interface BusyIfFalseProps : PropsWithChildren {
  var busy: Boolean
}
