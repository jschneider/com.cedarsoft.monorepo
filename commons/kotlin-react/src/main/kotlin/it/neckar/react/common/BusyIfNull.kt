package it.neckar.react.common

import react.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Shows a busy indicator if the property is null
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

fun <T1, T2> RBuilder.busyIfNull(value1: T1?, value2: T2?, handler: RElementBuilder<*>.(T1, T2) -> Unit) {
  contract {
    callsInPlace(handler, InvocationKind.AT_MOST_ONCE)
  }

  return child(busyIfNull) {
    attrs {
      this.busy = value1 == null || value2 == null
    }

    if (value1 != null && value2 != null) {
      this.handler(value1, value2)
    }
  }
}

val busyIfNull: FC<BusyIfNullProps> = fc("busyIfNull") { props ->
  if (props.busy == true) {
    busyIndicator {}
  } else {
    props.children()
  }
}

external interface BusyIfNullProps : PropsWithChildren {
  var busy: Boolean?
}
