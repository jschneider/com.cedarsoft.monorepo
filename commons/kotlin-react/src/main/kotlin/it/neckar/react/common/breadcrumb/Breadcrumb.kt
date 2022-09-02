package it.neckar.react.common.breadcrumb

import com.cedarsoft.common.collections.fastForEach
import react.*
import react.dom.*

/**
 * Shows the breadcrumb
 */
fun RBuilder.breadcrumb(
  /**
   * The leaf (on the right side) of the bread crumb
   */
  navigationState: NavigationState,
): Unit =
  child(breadcrumb) {
    attrs {
      this.navigationState = navigationState
    }
  }

val breadcrumb: FC<BreadcrumbProps> = fc("breadcrumb") { props ->
  val navigationState = props.navigationState
  val navigationStates = navigationState.getChainRecursively()

  nav {
    ol("breadcrumb") {

      navigationStates.fastForEach { navigationState ->
        li("breadcrumb-item") {
          navigationState.breadcrumbContent.invoke(this, navigationState)
        }
      }
    }
  }
}

external interface BreadcrumbProps : Props {
  var navigationState: NavigationState
}
