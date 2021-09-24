package it.neckar.react.common

import react.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Shows a busy indicator if the property
 */
fun <T> RBuilder.busyIfNull(value: T?, handler: RElementBuilder<*>.(T) -> Unit) {
  contract {
    callsInPlace(handler, InvocationKind.AT_MOST_ONCE)
  }

  return child(busyIfNull) {
    attrs {
      this.busy = value == null
    }

    if (value != null) {
      this.handler(value)
    }
  }
}

val busyIfNull: FunctionComponent<BusyIfNullProps> = fc("busyIfNull") { props ->
  if (props.busy == true) {
    busyIndicator()
  } else {
    props.children()
  }
}

external interface BusyIfNullProps : PropsWithChildren {
  var busy: Boolean?
}
