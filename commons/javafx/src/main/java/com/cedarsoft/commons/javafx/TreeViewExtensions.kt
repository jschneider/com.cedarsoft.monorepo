package com.cedarsoft.commons.javafx

import javafx.scene.control.TreeItem

/**
 */

/**
 * Returns the item with the given value
 */
fun <T> TreeItem<T>?.findItem(matcher: (TreeItem<T>) -> Boolean): TreeItem<T>? {
  if (this == null) {
    return null
  }

  //Check if this matches the matcher
  if (matcher(this)) {
    return this
  }

  //Now look for matches within the children
  for (child in children) {
    val result = child.findItem(matcher)
    if (result != null) {
      return result
    }
  }

  return null
}
