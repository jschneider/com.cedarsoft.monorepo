package it.neckar.common.redux


/**
 * Alias for a dispatch action.
 */
typealias  DispatchAction<S> = (StateAction<S>) -> Unit

/**
 * Dispatches a state action.
 * Uses the current store.
 *
 * Attention: It is required to call [Dispatch.registerDispatchAction] first
 */
fun <S> dispatch(action: StateAction<S>) {
  Dispatch.dispatchAction(action)
}

/**
 * Helper function to dispatch redux actions
 */
object Dispatch {
  /**
   * Throws an exception - is registered as fallback
   */
  private var failureDispatchAction: (StateAction<*>) -> Unit = {
    throw IllegalStateException("No dispatch action configured. Call Dispose.registerDispatchAction() first")
  }

  /**
   * The "global" dispatch action.
   *
   * Does not have generics! Will be cast!
   */
  var dispatchAction: DispatchAction<*> = failureDispatchAction
    private set


  /**
   * Registers the dispatch action.
   * The generics are just placeholders to simplify calls to this method.
   */
  fun <S> registerDispatchAction(newDispatchAction: DispatchAction<S>) {
    dispatchAction = newDispatchAction as DispatchAction<*>
  }

  fun disposeDispatchAction() {
    dispatchAction = failureDispatchAction
  }
}
