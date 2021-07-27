package com.cedarsoft.dispose

import com.cedarsoft.common.collections.fastForEach

/**
 * Holds actions that may be called upon dispose
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class DisposeSupport : Disposable, OnDispose {
  /**
   * The actions that are executed on dispose
   */
  private val disposeActions = mutableListOf<() -> Unit>()

  /**
   * Is set to true if dispose has been called
   */
  var disposed: Boolean = false
    private set

  /**
   * Registers an action that is executed when [dispose] is called
   */
  override fun onDispose(action: () -> Unit) {
    verifyNotDisposed()
    disposeActions.add(action)
  }

  /**
   * Executes all registered (by calling [onDispose]) actions.
   * Marks this as disposed ([disposed])
   */
  override fun dispose() {
    disposeActions.fastForEach {
      it()
    }

    //Now clear all actions that have been disposed
    disposeActions.clear()

    disposed = true
  }

  private fun verifyNotDisposed() {
    check(!disposed) {
      "Already disposed"
    }
  }
}
