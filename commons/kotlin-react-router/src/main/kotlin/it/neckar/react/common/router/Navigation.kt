package it.neckar.react.common.router

import com.cedarsoft.common.collections.fastForEach
import react.*
import react.dom.*
import react.router.*

/**
 * Represents a navigation element
 */
@NavigationDsl
data class NavigationElement(
  /**
   * The path as it is used in the route.
   *
   * Attention: Contains query parameters
   */
  val pathFragment: String,

  /**
   * Provides the element
   */
  val element: () -> ReactElement<out Props>,

  /**
   * Reference to the parent
   */
  val parent: NavigationElement?,

  val index: Boolean?,

  /**
   * Contains the information about the breadcrumb - if there is any
   */
  val breadcrumbInfo: BreadcrumbInfo? = null,
) {

  /**
   * The routes are later set - as soon as they are build
   */
  lateinit var routes: List<NavigationElement>

  /**
   * Returns all routes
   */
  fun routesRecursive(): List<NavigationElement> {
    return buildList {
      routes.fastForEach {
        add(it)
        addAll(it.routesRecursive())
      }
    }
  }


  /**
   * Returns the complete chain.
   * This is the *last* element within the returned list
   */
  fun getChain(): List<NavigationElement> {
    return buildList {
      var currentElement: NavigationElement? = this@NavigationElement
      while (currentElement != null) {
        add(0, currentElement)
        currentElement = currentElement.parent
      }
    }
  }

  fun completePath(): String {
    val chain = getChain()
    return chain.joinToString(separator = "/") { it.pathFragment }
  }


  @NavigationDsl
  class Builder(
    /**
     * The path fragment as it is used in the route
     */
    var pathFragment: String? = null,
  ) {
    /**
     * Provides the element
     */
    var element: (() -> ReactElement<out Props>)? = { Outlet.create() }

    var index: Boolean? = null

    /**
     * The routes - which may contain further notes
     */
    val routes: MutableList<Builder> = mutableListOf()

    var breadcrumbInfo: BreadcrumbInfo? = null

    /**
     * Registers the route
     */
    @NavigationDsl
    fun route(variable: RouterVar, handler: Builder.() -> Unit): Builder {
      return route(":${variable.value}", handler)
    }

    @NavigationDsl
    fun route(pathFragment: String, handler: Builder.() -> Unit): Builder {
      return Builder(pathFragment)
        .apply(handler)
        .also {
          routes.add(it)
        }
    }

    @NavigationDsl
    fun index(handler: Builder.() -> Unit): Builder {
      return Builder("")
        .apply(handler)
        .also {
          it.index = true
          routes.add(it)
        }
    }

    @NavigationDsl
    fun breadcrumb(handler: BreadcrumbInfo.Builder.() -> Unit) {
      this.breadcrumbInfo = BreadcrumbInfo.Builder()
        .also(handler)
        .build()
    }

    /**
     * Creates a new navigation element - does *not* build the children
     */
    fun build(parent: NavigationElement?): NavigationElement {
      return NavigationElement(
        pathFragment = requireNotNull(pathFragment) {
          "Path required"
        },
        element = requireNotNull(element) {
          "element required"
        },
        parent = parent,
        index = index,
        breadcrumbInfo = breadcrumbInfo
      )
    }

    fun buildRecursively(parent: NavigationElement?): NavigationElement {
      val thisElement = build(parent)

      val children = routes.map {
        it.buildRecursively(thisElement)
      }

      thisElement.routes = children
      return thisElement
    }
  }

}

/**
 * Builds the navigation
 */
@NavigationDsl
fun buildNavigation(block: NavigationRoot.Builder.() -> Unit): NavigationRoot {
  val builder = NavigationRoot.Builder()
    .apply(block)

  return builder.buildRecursively()
}


@NavigationDsl
data class NavigationRoot(
  /**
   * The routes - which may contain further notes
   */
  val routes: List<NavigationElement>,
) {
  /**
   * Contains all routes - flattened list
   */
  val allRoutesFlat: List<NavigationElement> = buildList {
    routes.fastForEach {
      add(it)
      addAll(it.routesRecursive())
    }
  }

  class Builder {
    /**
     * The routes - which may contain further notes
     */
    val routes: MutableList<NavigationElement.Builder> = mutableListOf()

    /**
     * Registers the route
     */
    @NavigationDsl
    fun route(pathFragment: String, handler: NavigationElement.Builder.() -> Unit): NavigationElement.Builder {
      check(pathFragment != "/") {
        "Instead of a single / use empty string"
      }

      return NavigationElement.Builder(pathFragment)
        .apply(handler)
        .also {
          routes.add(it)
        }
    }

    @NavigationDsl
    fun buildRecursively(): NavigationRoot {
      val children = routes.map {
        it.buildRecursively(null)
      }

      return NavigationRoot(children)

    }
  }
}


@DslMarker
annotation class NavigationDsl {
}


/**
 * Creates routes for the given navigation
 */
fun RBuilder.createRoutes(navigation: NavigationRoot, addFallbackRoute: Boolean = true) {
  Routes {
    if (addFallbackRoute) {
      Route {
        attrs {
          path = "*"
          element = NoRouteFoundComponent.create()
        }
      }
    }

    addRoutes(navigation)
  }
}

fun RBuilder.addRoutes(navigation: NavigationRoot) {
  navigation.routes.fastForEach {
    addRouteRecursively(it)
  }
}

/**
 * Adds all routes recursively
 */
private fun RBuilder.addRouteRecursively(navigationElement: NavigationElement) {
  Route {
    attrs {
      path = navigationElement.pathFragment
      element = navigationElement.element()

      navigationElement.index?.let {
        index = it
      }
    }

    //Recursive calls
    navigationElement.routes.fastForEach {
      addRouteRecursively(it)
    }
  }
}

/**
 * The breadcrumb info for this navigation element
 */
data class BreadcrumbInfo(
  val linkContent: (RBuilder.() -> Unit)?,
) {
  @NavigationDsl
  class Builder {
    var linkContent: (RBuilder.() -> Unit)? = null

    /**
     * Must not use hooks
     */
    var label: String
      get() {
        throw UnsupportedOperationException("only setter supported")
      }
      set(value) {
        linkContent = {
          linkContentComponent {
            span {
              +value
            }
          }
        }
      }

    /**
     * Must not use hooks
     */
    var icon: String
      get() {
        throw UnsupportedOperationException("only setter supported")
      }
      set(value) {
        linkContent = {
          linkContentComponent {
            i(classes = "$value px-1") {}
          }
        }
      }

    fun build(): BreadcrumbInfo {
      return BreadcrumbInfo(
        linkContent,
      )
    }
  }
}

val linkContentComponent: FC<PropsWithChildren> = fc("linkContentComponent") { props ->
  props.children()
}
