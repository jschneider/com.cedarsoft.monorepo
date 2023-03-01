package it.neckar.react.common.loading

import it.neckar.open.collections.fastForEach
import it.neckar.ktor.client.plugin.stats.PendingRequestsState
import it.neckar.commons.kotlin.js.safeGet
import react.*
import react.dom.*

val PendingRequestsStateInfo: FC<PendingRequestsStateInfoProps> = fc("ProgressRequestsInfo") { props ->
  val pendingRequestsState = props::pendingRequestsState.safeGet()

  div("row") {
    div("col-sm-12") {
      h3 { +"Started Requests" }

      pendingRequestsState.pendingRequests.fastForEach { entry ->
        div {
          +"${entry.method.value} ${entry.url}"
        }
      }

      pendingRequestsState.completedRequests.fastForEach { entry ->
        div {
          +"${entry.method.value} ${entry.url}  ✓"
        }
      }
    }
  }
}

external interface PendingRequestsStateInfoProps : Props {
  var pendingRequestsState: PendingRequestsState
}
