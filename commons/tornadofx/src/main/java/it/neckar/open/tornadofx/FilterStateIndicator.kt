package it.neckar.open.tornadofx

import javafx.beans.binding.ObjectBinding
import javafx.scene.Parent
import javafx.util.StringConverter
import tornadofx.*

/**
 * Indicator that visualizes the filter state
 */
class FilterStateIndicator(
  val filterStateProperty: ObjectBinding<FilterState>
) : Fragment() {

  override val root: Parent = hbox {
    label(filterStateProperty, converter = object : StringConverter<FilterState?>() {
      override fun toString(filterState: FilterState?): String {
        if (filterState == null) {
          return ""
        }

        return "${filterState.visible} / ${filterState.total}"
      }

      override fun fromString(string: String?): FilterState? {
        throw UnsupportedOperationException()
      }
    }) {
      toggleClass(CssRule.c("filtering"), filterStateProperty.isFilteredProperty())
    }
  }
}
