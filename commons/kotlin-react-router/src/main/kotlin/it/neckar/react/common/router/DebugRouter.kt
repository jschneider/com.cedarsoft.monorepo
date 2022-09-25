package it.neckar.react.common.router

import com.cedarsoft.common.kotlin.lang.ExecutionEnvironment
import react.*
import react.router.*


/**
 * Paints the current route in DEV mode
 */
val DebugRoute: FC<PropsWithChildren> = fc("DebugRouter") { props ->
  if (ExecutionEnvironment.isDev()) {
    //The environment does not change during runtime. Therefore, it is ok to call the hook conditinally
    val location = useLocation()
    console.log("Current Route: ${location.pathname}${location.search}, State:", location.state)
  }

  props.children()
}
