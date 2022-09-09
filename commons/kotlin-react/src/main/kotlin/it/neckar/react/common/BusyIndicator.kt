package it.neckar.react.common

import react.*
import react.dom.*

/**
 * Shows a busy indicator
 */
val busyIndicator: FC<BusyIndicatorProps> = fc("busyIndicator") {
  i("fa fa-spinner fa-spin my-3") {}
}

external interface BusyIndicatorProps : Props

