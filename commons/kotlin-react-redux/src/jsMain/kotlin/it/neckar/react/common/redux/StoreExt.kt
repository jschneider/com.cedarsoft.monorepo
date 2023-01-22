package it.neckar.react.common.redux

import it.neckar.common.redux.StateAction
import it.neckar.logging.Logger
import it.neckar.logging.LoggerFactory
import react.*
import react.redux.*
import redux.RAction
import redux.Store

/**
 * Generic store reduce action that understands [StateAction]s.
 */
fun <S> storeReduceAction(state: S, action: RAction?): S {
  if (action == null) {
    return state
  }

  return when (action) {
    is StateActionWrapper<*> -> {

      val stateActionWrapper = action as StateActionWrapper<S>
      val stateAction = stateActionWrapper.stateAction

      logger.debug("Reducing store with: ${stateAction::class.simpleName}")

      @Suppress("UNCHECKED_CAST")
      with(stateAction) {
        return state.updateState()
      }
    }

    else -> {
      logger.warn("Unsupported action: $action")
      state
    }
  }
}

/**
 * Redux Actions that automatically wraps a [StateAction]. Allows updating of the redux store
 */
data class StateActionWrapper<S>(
  val stateAction: StateAction<S>,
) : RAction

/**
 * Dispatches a state action
 */
fun <S, R> Store<S, RAction, R>.dispatch(action: StateAction<S>): R {
  return this.dispatch(StateActionWrapper(action))
}

private val logger: Logger = LoggerFactory.getLogger("it.neckar.react.common.redux.StoreExt")
