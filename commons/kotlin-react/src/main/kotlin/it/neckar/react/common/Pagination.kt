package it.neckar.react.common

import it.neckar.open.collections.fastForEach
import it.neckar.open.kotlin.lang.ceil
import it.neckar.open.kotlin.lang.floor
import it.neckar.react.common.*
import it.neckar.react.common.form.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import kotlin.math.roundToInt

/**
 *
 */
fun <T> RBuilder.pagination(

  currentPage: StateInstance<Int>,

  entryPages: List<List<T>>,

  paginationGroupSize: Int = 3,

  ): Unit = child(pagination) {
  attrs {
    this.currentPage = currentPage
    this.entryPages = entryPages.unsafeCast<List<List<Any>>>()
    this.paginationGroupSize = paginationGroupSize
  }
}


val pagination: FC<PaginationProps> = fc("pagination") { props ->

  val currentPage = props.currentPage
  val entryPages = props.entryPages
  val paginationGroupSize = props.paginationGroupSize


  nav {
    ul("pagination justify-content-center") {
      li("page-item") {
        addClassIf("disabled") { currentPage.value <= 0 }

        attrs {
          onClickFunction = {
            currentPage.setter((currentPage.value - 1).coerceAtLeast(0))
          }
        }

        div(classes = "page-link") {
          span("fas fa-angle-left") {}
        }
      }

      /**
       * If there are only a few pages, just display the normal pagination
       */
      if (entryPages.size <= paginationGroupSize * 3) {
        entryPages.fastForEach { page: List<Any> ->
          val index = entryPages.indexOf(page)
          tablePaginationButton(index, currentPage)
        }
      } else {
        /**
         * Display the first [paginationGroupSize] buttons
         */
        entryPages.take(paginationGroupSize).fastForEach { page: List<Any> ->
          val index = entryPages.indexOf(page)
          tablePaginationButton(index, currentPage)
        }

        /**
         * Add a separator if necessary
         */
        if (currentPage.value >= (paginationGroupSize * 1.5).roundToInt()) {
          li("page-item disabled") {
            div(classes = "page-link") {
              +"..."
            }
          }
        }

        /**
         * Fit a button group in the middle of the currently selected page is somewhere in the middle
         */
        if (currentPage.value in (paginationGroupSize - 1)..(entryPages.size - paginationGroupSize + 1)) {
          entryPages.subList(
            (currentPage.value - (paginationGroupSize * 0.5).floor().roundToInt()).coerceAtLeast(paginationGroupSize),
            (currentPage.value + (paginationGroupSize * 0.5).ceil().roundToInt()).coerceAtMost(entryPages.size - paginationGroupSize)
          ).fastForEach { page: List<Any> ->
            val index = entryPages.indexOf(page)
            tablePaginationButton(index, currentPage)
          }
        }

        /**
         * Add a separator if necessary
         */
        if (currentPage.value < (entryPages.size - paginationGroupSize * 1.5).roundToInt() - 1) {
          li("page-item disabled") {
            div(classes = "page-link") {
              +"..."
            }
          }
        }

        /**
         * Display the last [paginationGroupSize] buttons
         */
        entryPages.takeLast(paginationGroupSize).fastForEach { page: List<Any> ->
          val index = entryPages.indexOf(page)
          tablePaginationButton(index, currentPage)
        }
      }

      li("page-item") {
        addClassIf("disabled") { currentPage.value >= entryPages.size - 1 }

        attrs {
          onClickFunction = {
            currentPage.setter((currentPage.value + 1).coerceAtMost(entryPages.size - 1))
          }
        }

        div(classes = "page-link") {
          span("fas fa-angle-right") {}
        }
      }
    }
  }
}

private fun RBuilder.tablePaginationButton(page: Int, currentPage: StateInstance<Int>) {
  li("page-item") {
    addClassIf("active") { page == currentPage.value }

    attrs {
      onClickFunction = {
        currentPage.setter(page)
      }
    }

    div(classes = "page-link") {
      +"${page + 1}"
    }
  }
}


external interface PaginationProps : Props {

  var entryPages: List<List<Any>>

  var currentPage: StateInstance<Int>

  var paginationGroupSize: Int
}
