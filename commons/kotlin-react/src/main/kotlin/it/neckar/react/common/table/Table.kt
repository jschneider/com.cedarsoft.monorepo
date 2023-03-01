package it.neckar.react.common.table

import it.neckar.open.collections.fastForEach
import it.neckar.commons.kotlin.js.safeGet
import it.neckar.react.common.*
import kotlinx.html.TD
import kotlinx.html.TH
import kotlinx.html.ThScope
import react.*
import react.dom.*

/**
 *
 */
fun <T> RBuilder.table(
  /**
   * List of the [T]s to be displayed
   */
  entries: List<T>?,

  /**
   * First column of the table
   *  the first column on an HTML table has a different class, hence the differentiation
   */
  firstColumn: TableFirstColumn<T>,
  /**
   * List of information how what columns to display and how to format their data
   */
  columns: List<TableColumn<T>>,

  handler: (TableProps) -> Unit = {},

  ): Unit = child(Table) {
  attrs {
    this.tableEntries = entries.unsafeCast<List<Any>>()
    this.firstColumn = firstColumn.unsafeCast<TableFirstColumn<Any>>()
    this.columns = columns.unsafeCast<List<TableColumn<Any>>>()

    handler(this)
  }
}


val Table: FC<TableProps> = fc("Table") { props ->
  val tableEntries = props::tableEntries.safeGet()
  val firstColumn = props::firstColumn.safeGet()
  val columns = props::columns.safeGet()


  busyIfNull(tableEntries) { entries ->
    table(classes = "table table-hover table-responsive align-middle") {
      thead {
        tr {
          th(scope = ThScope.col) {
            +firstColumn.title()
          }
          columns.fastForEach { tableColumn ->
            th(scope = ThScope.col) {
              +tableColumn.title()
            }
          }
        }
      }


      tbody {
        entries.fastForEach { entry ->
          tr {
            th(scope = ThScope.row, block = firstColumn.block(entry))

            columns.fastForEach { tableColumn ->
              td(block = tableColumn.block(entry))
            }
          }
        }
      }

    }
  }
}

data class TableFirstColumn<T>(
  val title: () -> String,
  val block: (T) -> (RDOMBuilder<TH>.() -> Unit),
)

data class TableColumn<T>(
  val title: () -> String,
  val block: (T) -> (RDOMBuilder<TD>.() -> Unit),
)


external interface TableProps : Props {
  /**
   * List of the entries to be displayed
   * can be null and shows busy indicator
   */
  var tableEntries: List<Any>?

  var firstColumn: TableFirstColumn<Any>

  /**
   * List of information how what columns to display and how to format their data
   */
  var columns: List<TableColumn<Any>>
}
