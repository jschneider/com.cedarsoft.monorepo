package it.neckar.react.common.redux

import react.*
import react.redux.*
import redux.RAction


/**
 * Generic store reduce action that understands [StateAction]s.
 */
fun <S> storeReduceAction(state: S, action: RAction?): S {
  if (action == null) {
    return state
  }

  println("Reducing store with: ${action::class.simpleName}")

  return when (action) {
    is StateAction<*> -> {
      @Suppress("UNCHECKED_CAST")
      with(action as StateAction<S>) {
        return state.updateState()
      }
    }

    else -> {
      println("Unsupported action: $action")
      state
    }
  }
}

/**
 * Redux Actions that automatically updates the global app state
 */
interface StateAction<S> : RAction {
  /**
   * Returns the updated state
   */
  fun S.updateState(): S
}

/**
 * Helper method to simplify usage of the [useDispatch] hook - without having to define the generics.
 */
inline fun useDispatch(): (RAction) -> dynamic {
  return react.redux.useDispatch()
}
