package com.cedarsoft.commons.javafx

import javafx.scene.canvas.GraphicsContext

/**
 * Saves the state of the gc and restores it afterwards
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class WithSavedGc(
  val gc: GraphicsContext
) {

  inline fun paint(action: (gc: GraphicsContext) -> Unit) {
    gc.save()
    try {
      action(gc)
    } finally {
      gc.restore()
    }
  }
}

/**
 * Executes the action within a saved context
 */
fun GraphicsContext.saved(action: (gc: GraphicsContext) -> Unit) {
  save()
  try {
    action(this)
  } finally {
    restore()
  }

}
