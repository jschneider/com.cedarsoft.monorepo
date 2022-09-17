package it.neckar.react.common.router

import com.cedarsoft.common.collections.fastForEach
import it.neckar.commons.kotlin.js.Environment
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
   * Returns all routes recursively
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
  fun getChain(stopAtAbsoluteElements: Boolean): List<NavigationElement> {
    return buildList {
      var currentElement: NavigationElement? = this@NavigationElement
      while (currentElement != null) {
        add(0, currentElement)

        if (stopAtAbsoluteElements && currentElement.isAbsolute()) {
          //Return immediately if this is an absolute URL
          return@buildList
        }
        currentElement = currentElement.parent
      }
    }
  }

  /**
   * Returns true if this element is absolute
   */
  fun isAbsolute(): Boolean {
    return pathFragment.startsWith("/")
  }

  fun completePath(): String {
    val chain = getChain(true)
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
     * Can be used to overwrite the parent URL.
     * This can be used when parallel routes are used to solve issues related to Outlets.
     *
     * The parent URL is then used to identify the parent for the breadcrumb url.
     *
     * ATTENTION: The order matters. The parent must be created *before* it is referenced.
     */
    var parentUrl: String? = null

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
    fun build(parent: NavigationElement?, context: NavigationBuildingContext): NavigationElement {
      if (parentUrl != null) {
        pathFragment.let {
          require(it != null && it.startsWith("/")) { "Use absolute URLs if the parent URL is configured" }
        }
      }

      //Resolve the parent if necessary
      val parentToUse: NavigationElement? = this.parentUrl?.let {
        context.findElement(it)
      } ?: parent

      return NavigationElement(
        pathFragment = requireNotNull(pathFragment) {
          "Path required"
        },
        element = requireNotNull(element) {
          "element required"
        },
        parent = parentToUse,
        index = index,
        breadcrumbInfo = breadcrumbInfo
      )
    }

    fun buildRecursively(parent: NavigationElement?, context: NavigationBuildingContext): NavigationElement {
      val thisElement = build(parent, context)
      context.store(thisElement)

      val children = routes.map {
        it.buildRecursively(thisElement, context)
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

  return builder.buildRecursively(NavigationBuildingContext())
}

/**
 * Helper class that is used to build the navigation.
 *
 * This object is used to resolve parents
 */
class NavigationBuildingContext {
  /**
   * Returns the navigation element for the given URL
   */
  fun findElement(url: String): NavigationElement {
    return url2element[url] ?: throw IllegalArgumentException("No element found in NavigationBuildingContext for <$url>")
  }

  private val url2element = mutableMapOf<String, NavigationElement>()

  fun store(element: NavigationElement) {
    url2element[element.completePath()] = element
  }
}

@NavigationDsl
data class NavigationRoot(
  /**
   * The (direct) routes - which may contain further routes
   */
  val directChildren: List<NavigationElement>,
  /**
   * Contains all routes - flattened list
   */
  val allRoutes: List<NavigationElement> = buildList {
    directChildren.fastForEach {
      add(it)
      addAll(it.routesRecursive())
    }
  },
) {

  class Builder {
    /**
     * The routes - which may contain further notes
     */
    val children: MutableList<NavigationElement.Builder> = mutableListOf()

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
          children.add(it)
        }
    }

    @NavigationDsl
    fun buildRecursively(context: NavigationBuildingContext): NavigationRoot {
      val children = children.map {
        it.buildRecursively(null, context)
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

/**
 * Adds the route from the provided navigation root
 */
fun RBuilder.addRoutes(navigationRoot: NavigationRoot) {
  navigationRoot.directChildren.fastForEach {
    addRouteRecursively(it)
  }

  if (Environment.Dev) {
    val allRoutes = navigationRoot.allRoutes
    console.log("---------- Added ${allRoutes.size} routes ---------")
    allRoutes.fastForEach {
      console.log(it.completePath())
    }
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
    /**
     * If the link content is set directly, [label] and [icon] are ignored!
     *
     * Attention: To avoid hook problems, do *not* use hooks directly in the provided lambda.
     * Instead, create new functional components that call hooks.
     */
    var linkContent: (RBuilder.() -> Unit)? = null

    /**
     * This value is only used if [linkContent] is *not* set
     */
    var label: String? = null

    /**
     * This value is only used if [linkContent] is *not* set
     */
    var icon: String? = null

    fun build(): BreadcrumbInfo {
      return BreadcrumbInfo(
        createLinkContent(),
      )
    }

    private fun createLinkContent(): (RBuilder.() -> Unit)? {
      if (this.linkContent != null) {
        //Is configured manually
        return this.linkContent
      }

      //Fallback that uses the [label] and [icon]
      require(label != null || icon != null) {
        "If no link content is configured, a label and/or an icon must be set"
      }

      return {
        LinkContentComponent {
          icon?.let {
            i(classes = "$it px-1") {}
          }

          label?.let {
            +it
          }
        }
      }
    }
  }
}

val LinkContentComponent: FC<PropsWithChildren> = fc("linkContentComponent") { props ->
  props.children()
}
