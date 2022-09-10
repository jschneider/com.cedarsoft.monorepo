package it.neckar.react.common.router

import com.cedarsoft.common.collections.fastForEach
import it.neckar.react.common.*
import it.neckar.react.common.annotations.*
import react.*
import react.dom.*
import react.dom.aria.*
import react.router.*
import react.router.dom.*


/**
 * Creates a breadcrumb bar
 */
val breadcrumbBar: FC<BreadcrumbBarProps> = fc("breadcrumbBar") { props ->
  check(useInRouterContext()) {
    "Must only be used in router context"
  }

  val navigation = requireNotNull(props.navigation) { "attrs.navigation required" }
  val matchingNavigationElement = navigation.useFindBestRoute()

  val location = useLocation()
  checkNotNull(matchingNavigationElement) {
    "No navigation element found for ${location.pathname}"
  }

  //Render the breadcrumb
  nav {
    ol("breadcrumb") {

      breadcrumbElements {
        attrs {
          this.navigationElement = matchingNavigationElement
        }
      }
    }
  }
}

external interface BreadcrumbBarProps : Props {
  var navigation: NavigationRoot?
}


/**
 * Renders the breadcrumb elements
 */
val breadcrumbElements: FC<BreadcrumbElementsProps> = fc("breadcrumbElements") { props ->
  props.navigationElement.getChain().fastForEach { navigationElement ->
    breadCrumbElement {
      attrs {
        this.navigationElement = navigationElement
      }
    }
  }
}

external interface BreadcrumbElementsProps : Props {
  var navigationElement: NavigationElement
}

/**
 * Creates a single breadcrumb element ([li] containing a [NavLink])
 */
val breadCrumbElement: FC<BreadCrumbElementProps> = fc("breadCrumbElement") { props ->
  val navigationElement = requireNotNull(props.navigationElement) { "props.navigationElement required" }
  val breadcrumbInfo = navigationElement.breadcrumbInfo

  val linkContent = breadcrumbInfo?.linkContent ?: {
    span {
      //No breadcrumb info - we use the path fragment
      +navigationElement.pathFragment
    }
  }


  li("breadcrumb-item") {
    NavLink {
      linkContent()

      attrs {
        to = generatePath(navigationElement.completePath(), useParams())
        end = true
      }
    }
  }
}

external interface BreadCrumbElementProps : Props {
  var navigationElement: NavigationElement?
}


/**
 * Find the best navigation element for the current route.
 *
 * ATTENTION: We have to use filter + iterate over *all* routes to keep the number of hooks constant
 */
@UsesHooks
fun NavigationRoot.useFindBestRoute(): NavigationElement? {
  @Suppress("SimplifiableCallChain")
  return allRoutesFlat.filter {
    val completePath = it.completePath()
    val useMatch = useMatch(completePath)
    if (false) {
      console.log("useMatch for $completePath", useMatch)
    }

    //Check if there is a match *and* this is an "end match"
    useMatch != null && useMatch.pattern.end == true
  }.firstOrNull()
}
