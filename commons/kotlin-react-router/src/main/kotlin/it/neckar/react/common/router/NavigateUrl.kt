package it.neckar.react.common.router

/**
 * Represents a URL that can be used to navigate using the React Router
 */
value class NavigateUrl(val value: String) {
  override fun toString(): String {
    return value
  }

  companion object {
    /**
     * Empty path - can be used for default routes
     */
    val empty: NavigateUrl = NavigateUrl("")
  }
}

/**
 * Extension property for URLs
 */
var react.router.dom.LinkProps.toUrl: NavigateUrl
  get() {
    return NavigateUrl(to)
  }
  set(value) {
    this.to = value.value
  }
