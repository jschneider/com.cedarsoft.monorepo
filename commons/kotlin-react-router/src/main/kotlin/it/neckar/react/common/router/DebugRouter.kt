package it.neckar.react.common.router

import it.neckar.commons.kotlin.js.Environment
import react.*
import react.router.*


/**
 * Paints the current route in DEV mode
 */
val DebugRoute: FC<PropsWithChildren> = fc("DebugRouter") { props ->
  if (Environment.Dev) {
    //The environment does not change during runtime. Therefore, it is ok to call the hook conditinally
    val location = useLocation()
    console.log("Current Route: ${location.pathname}${location.search}, State:", location.state)
  }

  props.children()
}
