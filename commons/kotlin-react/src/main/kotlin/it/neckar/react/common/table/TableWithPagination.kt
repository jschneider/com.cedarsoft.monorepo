package it.neckar.react.common.table

import it.neckar.react.common.*
import it.neckar.react.common.form.*
import react.*
import react.dom.*

/**
 *
 */
fun <T> RBuilder.tableWithPagination(
  entries: List<T>,
  firstColumn: TableFirstColumn<T>,
  columns: List<TableColumn<T>>,
  doubleClickAction: (T) -> Unit,
  initialPageSize: Int,
  handler: (TableProps) -> Unit = {},
) {

  val pageSize: StateInstance<Int?> = useState(initialPageSize)
  val currentPage = useState(0)

  val entryPages: List<List<T>> = useMemo(entries, pageSize.value) {
    entries.chunked(pageSize.value ?: entries.size.coerceAtLeast(1))
  }

  useMemo(entryPages.size) {
    currentPage.setter(currentPage.value.coerceAtMost((entryPages.size - 1).coerceAtLeast(0)))
  }

  /**
   * if the list of projects is empty pass empty list to the table
   * */
  val entriesOnCurrentPage = useMemo(entries, currentPage.value) {
    if (entries.isEmpty()) {
      emptyList()
    } else {
      entryPages[currentPage.value.coerceAtMost((entryPages.size - 1).coerceAtLeast(0))]
    }
  }


  table(entriesOnCurrentPage, firstColumn, columns, doubleClickAction, handler)

  div("row") {
    div("col-2") {
      floatingSelectNullable(
        valueAndSetter = pageSize,
        idProvider = { int -> int?.toString() ?: "allProjects" },
        formatter = { int -> int?.toString() ?: "ALLE - ${entries.size}" },
        availableOptionsWithoutNull = listOf(5, 10, 15, 20, 25, 100),
        fieldName = "pageSizeField",
        title = "Projektanzahl",
      )
    }

    div("col-10") {
      pagination(currentPage, entryPages)
    }
  }

}
