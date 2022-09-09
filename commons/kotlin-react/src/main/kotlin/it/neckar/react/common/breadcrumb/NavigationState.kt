package it.neckar.react.common.breadcrumb

import it.neckar.react.common.annotations.*
import kotlinx.html.DIV
import kotlinx.html.LI
import react.dom.*


/**
 * Describes "where" the user is currently located.
 * The navigation state can be converted to a URL and back.
 *
 * All implementations should start with "Show".
 *
 * A navigation state item represents one entry in the bread crumb. Each element knows its parent.
 * The breadcrumb will be generated starting from the leaf (on the right). And the traversing
 * to the parents (like a linked list).
 */
@Deprecated("Navigation state is held in URLs")
abstract class NavigationState(
  /**
   * The renderer for the breadcrumb itself
   */
  val breadcrumbContent: @UsesHooks BreadcrumbContent,

  /**
   * Builder for the content of the page for the active navigation.
   *
   * This lambda will be called when rendering the page itself - not the bread crumb
   */
  val pageContent: @UsesHooks RDOMBuilder<DIV>.() -> Unit,

  /**
   * Provides the parent in the crumb bar.
   */
  val parent: @UsesHooks (() -> NavigationState)?,
) {

  /**
   * Encodes the navigation state into a relative URL
   */
  open fun toUrl(): String {
    //TODO make abstract!
    return "TODO implement me"
  }
}

/**
 * Returns a list containing all navigation states.
 * This is the last element of the list
 */
@Deprecated("Navigation state is held in URLs")
fun NavigationState.getChainRecursively(): List<NavigationState> {
  return buildList {
    //iterate recursively over all parents

    var currentElement: NavigationState? = this@NavigationState
    while (currentElement != null) {
      //at the start of the list (parents come first in the breadcrumb bar)
      add(0, currentElement)

      currentElement = currentElement.parent?.invoke()
    }
  }
}


/**
 * Adds the content to the breadcrumb itself (e.g. creates a link or an icon)
 */
typealias BreadcrumbContent = RDOMBuilder<LI>.(navigationState: NavigationState) -> Unit
