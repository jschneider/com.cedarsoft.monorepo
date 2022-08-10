package it.neckar.lifeCycle

/**
 * Describes elements that have a life cycle
 */
interface HasLifeCycle {
  /**
   * The lifecycle state for the element
   */
  val lifeCycleState: LifeCycleState


  fun isActive(): Boolean {
    return lifeCycleState.isActive()
  }

  fun isEndOfLife(): Boolean {
    return lifeCycleState.isEndOfLife()
  }
}

/**
 * Returns a list containing only the active elements
 */
fun <T : HasLifeCycle> List<T>.onlyActive(): List<T> {
  return only(LifeCycleState.Active)
}

/**
 * Returns only the elements with the [expectedLifecycleState].
 * If the [expectedLifecycleState] is set to null, all elements are returned.
 */
fun <T : HasLifeCycle> List<T>.only(expectedLifecycleState: LifeCycleState?): List<T> {
  if (expectedLifecycleState == null) {
    return this
  }

  return this.filter {
    it.lifeCycleState == expectedLifecycleState
  }
}
