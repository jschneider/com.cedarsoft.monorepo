package it.neckar.react.common.table

import com.cedarsoft.common.collections.fastForEach
import kotlinx.html.TD
import kotlinx.html.TH
import kotlinx.html.ThScope
import kotlinx.html.js.onDoubleClickFunction
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

  doubleClickAction: (T) -> Unit,

  handler: (TableProps) -> Unit = {},

  ): Unit = child(table) {
  attrs {
    this.entries = entries.unsafeCast<List<Any>>()
    this.firstColumn = firstColumn.unsafeCast<TableFirstColumn<Any>>()
    this.columns = columns.unsafeCast<List<TableColumn<Any>>>()
    this.doubleClickAction = doubleClickAction.unsafeCast<(Any) -> Unit>()

    handler(this)
  }
}


val table: FC<TableProps> = fc("table") { props ->

  table(classes = "table table-hover table-responsive align-middle") {
    thead {
      tr {
        th(scope = ThScope.col) {
          +props.firstColumn.title()
        }
        props.columns.fastForEach { tableColumn ->
          th(scope = ThScope.col) {
            +tableColumn.title()
          }
        }
      }
    }

    tbody {
      props.entries?.fastForEach { entry ->
        tr {
          attrs {
            onDoubleClickFunction = props.doubleClickAction
          }

          th(scope = ThScope.row, block = props.firstColumn.block(entry))

          props.columns.fastForEach { tableColumn ->
            td(block = tableColumn.block(entry))
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
   */
  var entries: List<Any>?

  var firstColumn: TableFirstColumn<Any>

  /**
   * List of information how what columns to display and how to format their data
   */
  var columns: List<TableColumn<Any>>

  var doubleClickAction: (Any) -> Unit
}
