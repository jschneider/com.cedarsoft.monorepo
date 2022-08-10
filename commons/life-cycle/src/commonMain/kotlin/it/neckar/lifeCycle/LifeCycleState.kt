package it.neckar.lifeCycle

import com.cedarsoft.common.kotlin.lang.toCheckboxChar
import it.neckar.lifeCycle.LifeCycleState.EndOfLife
import kotlinx.serialization.Serializable

/**
 * Describes the life cycle state for an element.
 *
 * Elements that are marked as [EndOfLife] must not be selected / published anymore.
 * But they may still be selected and therefore must still exist.
 *
 * This allows the user to select values that match the old element as close as possible.
 */
@Serializable
enum class LifeCycleState {
  /**
   * The element is active. It can be used.
   */
  Active,

  /**
   * The element is no longer supported.
   * It has been supported - but is no longer
   */
  EndOfLife

  ;

  /**
   * Returns the combined ("worst") life cycle from two life cycles.
   *
   * Only returns [Active] if both are [Active]
   */
  fun and(other: LifeCycleState?): LifeCycleState {
    if (other == null) {
      return this
    }

    if (this == EndOfLife || other == EndOfLife) {
      return EndOfLife
    }

    require(this == Active && other == Active) {
      "Unexpected combination this: <$this> other: <$other>"
    }

    return Active
  }

  fun isActive(): Boolean {
    return this == Active
  }

  fun isEndOfLife(): Boolean {
    return this == EndOfLife
  }

  fun format(): String {
    return when (this) {
      Active -> true.toCheckboxChar()
      EndOfLife -> false.toCheckboxChar()
    }
  }

}
