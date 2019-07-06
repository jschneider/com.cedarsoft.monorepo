package com.cedarsoft.commons.javafx

import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.collections.ObservableList

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

/**
 * Converts an observable list to a ReadOnlyListProperty
 */
fun <E> ObservableList<E>.readOnly(): ReadOnlyListProperty<E>? {
  return ReadOnlyListWrapper(this)
}
