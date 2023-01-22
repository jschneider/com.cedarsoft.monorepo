package it.neckar.common.redux

/**
 * Redux Actions that automatically updates the global app state
 */
interface StateAction<S> {
  /**
   * Returns the updated state
   */
  fun S.updateState(): S
}

