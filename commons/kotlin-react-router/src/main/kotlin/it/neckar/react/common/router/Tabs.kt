package it.neckar.react.common.router

import com.cedarsoft.common.collections.fastForEach
import it.neckar.react.common.*
import it.neckar.react.common.router.TabButtonStatus.*
import kotlinx.css.*
import kotlinx.html.UL
import kotlinx.html.role
import react.*
import react.dom.*
import react.dom.aria.*
import react.router.*
import react.router.dom.*
import styled.*

enum class TabButtonStatus {
  Normal,
  Disabled,
}

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
fun RBuilder.tabButton(name: String, url: NavigateUrl, tabButtonStatus: TabButtonStatus, end: Boolean = true) {
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

        if (tabButtonStatus == Disabled) {
          addClass("disabled")
        }
      }
    }
  }
}


/**
 * Creates Router Tabs
 */
fun RBuilder.routerTabs(block: RouterTabsConfig.Builder.() -> Unit) {
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
          tabButton(it.tabTitle, it.path, it.disabled)
        }
      }

      Routes {
        tabs.fastForEach { routerTabConfig ->
          Route {
            attrs {
              path = routerTabConfig.path.value
              element = createElement<Props> {
                styledDiv {
                  css {
                    overflow = Overflow.auto
                  }
                  child(routerTabConfig.tabContent())
                }
              }
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
      disabled: TabButtonStatus = Normal,
      element: (() -> ReactElement<out Props>),
    ) {
      this.tabs.add(RouterTabConfig(tabTitle, path, disabled, element))
    }

    fun build(): RouterTabsConfig {
      return RouterTabsConfig(
        tabs.toList()
      )
    }
  }
}

/**
 * Configuration for a router tab
 */
class RouterTabConfig(
  /**
   * The title for the tab - visible to the user
   */
  val tabTitle: String,
  /**
   * The navigation path (usually relative) that is used to activate the tab
   */
  val path: NavigateUrl,
  /**
   * If the tab button is disabled
   */
  val disabled: TabButtonStatus,
  /**
   * Provides the content of the tab
   */
  val tabContent: (() -> ReactElement<out Props>),
)
