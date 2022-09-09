package it.neckar.react.common.router

import react.router.*

/**
 * Navigates one back:
 *
 * Usage like this:
 * `useNavigation().back()`
 */
fun NavigateFunction.back() {
  this.invoke(-1)
}

/**
 * Navigates to the given url
 */
operator fun NavigateFunction.invoke(url: NavigateUrl) {
  this.invoke(url.value)
}

/**
 * Actions that are executed using the [NavigateFunction] as base
 */
typealias NavigationAction = NavigateFunction.() -> Unit

