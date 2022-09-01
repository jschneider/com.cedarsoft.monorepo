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

  fun isNearEndOfLive(): Boolean {
    return lifeCycleState.isNearEndOfLive()
  }

  fun isNotEndOfLife(): Boolean {
    return lifeCycleState.isNotEndOfLife()
  }

  fun isEndOfLife(): Boolean {
    return lifeCycleState.isEndOfLife()
  }
}

/**
 * Returns a list containing only the active elements
 */
fun <T : HasLifeCycle> List<T>.onlyActive(): List<T> {
  return only(LifeCycleState::isNotEndOfLife)
}

/**
 * Returns only the elements for with the [selector] returns [true].
 * If the [selector] is set to null, all elements are returned.
 */
fun <T : HasLifeCycle> List<T>.only(selector: ((LifeCycleState) -> Boolean)?): List<T> {
  if (selector == null) return this

  return this.filter {
    selector(it.lifeCycleState)
  }
}
