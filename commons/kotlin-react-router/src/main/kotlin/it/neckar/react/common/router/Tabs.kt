package it.neckar.react.common.router

import com.cedarsoft.common.collections.fastForEach
import it.neckar.react.common.*
import kotlinx.html.UL
import kotlinx.html.role
import react.*
import react.dom.*
import react.dom.aria.*
import react.router.*
import react.router.dom.*


/**
 * The tab pane should be used as container for [tabButton] calls
 */
fun RBuilder.tabButtonsPane(block: RDOMBuilder<UL>.() -> Unit) {
  ul("nav nav-tabs") {
    block()
  }
}

/**
 * Creates a tab button - using react router.
 * Should be placed within a [tabButtonsPane].
 */
fun RBuilder.tabButton(name: String, url: NavigateUrl, end: Boolean = true) {
  li("nav-item") {
    attrs {
      role = "presentation"
    }

    NavLink {
      +name

      attrs {
        addClass("nav-link")
        toUrl = url

        this.end = end
        role = AriaRole.tab
      }
    }
  }
}


/**
 * Creates Router Tabs
 */
fun RBuilder.RouterTabs(block: RouterTabsConfig.Builder.() -> Unit) {
  val config = RouterTabsConfig.Builder()
    .also(block)
    .build()

  config.create(this)
}

/**
 * Contains the configuration for Router tabs
 */
class RouterTabsConfig(
  val tabs: List<RouterTabConfig>,
) {
  fun create(builder: RBuilder) {
    with(builder) {
      tabButtonsPane {
        tabs.fastForEach {
          tabButton(it.tabTitle, it.path)
        }
      }

      Routes {
        tabs.fastForEach {
          Route {
            attrs {
              path = it.path.value
              element = it.element()
            }
          }
        }
      }
    }
  }

  class Builder {
    val tabs: MutableList<RouterTabConfig> = mutableListOf()

    /**
     * Creates a new tab
     */
    fun tab(
      path: NavigateUrl,
      tabTitle: String,
      element: (() -> ReactElement<out Props>),
    ) {
      this.tabs.add(RouterTabConfig(tabTitle, path, element))
    }

    fun build(): RouterTabsConfig {
      return RouterTabsConfig(
        tabs.toList()
      )
    }
  }
}

class RouterTabConfig(
  val tabTitle: String,
  val path: NavigateUrl,
  val element: (() -> ReactElement<out Props>),
) {
}
