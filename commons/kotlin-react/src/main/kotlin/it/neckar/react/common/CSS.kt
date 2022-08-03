package it.neckar.react.common

import kotlinx.html.CommonAttributeGroupFacade

/**
 *
 */
object CSS {
  /**
   * Input fields that are merged to the input field below
   */
  const val mergedBelow: String = "mergedBelow"

  /**
   * Input fields that are merged to the input field above
   */
  const val mergedAbove: String = "mergedAbove"

  /**
   * Input fields that are merged to other input fields to the right
   */
  const val mergedRight: String = "mergedRight"

  /**
   * Input fields that are merged to other input fields to the left
   */
  const val mergedLeft: String = "mergedLeft"

}


fun CommonAttributeGroupFacade.mergedAbove() {
  addClass(CSS.mergedAbove)
}

fun CommonAttributeGroupFacade.mergedBelow() {
  addClass(CSS.mergedBelow)
}

fun CommonAttributeGroupFacade.mergedRight() {
  addClass(CSS.mergedRight)
}

fun CommonAttributeGroupFacade.mergedLeft() {
  addClass(CSS.mergedLeft)
}
