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

val busyIndicator: FC<BusyIndicatorProps> = fc("busyIndicator") { props ->
  i("fa fa-spinner fa-spin") {}
}

external interface BusyIndicatorProps : Props {
  var daName: String
}

