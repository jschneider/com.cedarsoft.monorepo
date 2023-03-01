package it.neckar.open.javafx

import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.collections.ObservableList

/**
 */

/**
 * Converts an observable list to a ReadOnlyListProperty
 */
fun <E> ObservableList<E>.readOnly(): ReadOnlyListProperty<E>? {
  return ReadOnlyListWrapper(this)
}
