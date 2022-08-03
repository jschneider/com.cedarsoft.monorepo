package it.neckar.react.common

import react.*
import react.dom.*

/**
 *
 */
fun RBuilder.busyIndicator(handler: BusyIndicatorProps.() -> Unit = {}): Unit = child(busyIndicator) {
  attrs {
    handler()
  }
}

val busyIndicator: FC<BusyIndicatorProps> = fc("busyIndicator") {
  i("fa fa-spinner fa-spin my-3") {}
}

external interface BusyIndicatorProps : Props

