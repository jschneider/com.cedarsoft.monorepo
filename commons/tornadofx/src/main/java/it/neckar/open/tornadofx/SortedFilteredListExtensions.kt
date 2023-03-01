package it.neckar.open.tornadofx

import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ObservableValue
import tornadofx.*
import java.util.concurrent.Callable

/**
 */

/**
 * Returns the filter count for the list
 */
fun SortedFilteredList<*>.filterCount(): FilterState {
  return FilterState(items.size, filteredItems.size)
}

fun SortedFilteredList<*>.filterCountProperty(): ObjectBinding<FilterState> {
  return Bindings.createObjectBinding(
    Callable<FilterState> { filterCount() }
    , items, filteredItems
  )
}

/**
 * The filter state
 */
data class FilterState(
  val total: Int,
  val visible: Int
) {
  fun isFiltering(): Boolean? {
    return visible < total
  }
}

fun ObjectBinding<FilterState>.isFilteredProperty(): ObservableValue<Boolean> {
  return Bindings.createBooleanBinding(Callable {
    this.get().isFiltering()
  }, this)
}
