package it.neckar.react.common.router

import react.*
import react.dom.*
import react.router.*

/**
 * Default placeholder component that can be used to visualize that no route has been found
 */
val NoRouteFoundComponent: FC<Props> = fc("NoRouteFoundComponent") { props ->
  div {
    p {
      +"No route could be found for ${useLocation().pathname}"
    }
  }
}
