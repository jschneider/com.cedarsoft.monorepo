package it.neckar.react.common.form

import it.neckar.open.unit.other.Inclusive

/**
 * A sealed interface defining number constraints that can be applied to both integers and doubles.
 */
sealed interface NumberConstraint {
  /**
   * Applies a constraint to an integer value.
   *
   * @param value the integer value to apply the constraint to.
   * @return the constrained integer value.
   */
  fun constraint(value: Int): Int

  /**
   * Applies a constraint to a double value.
   *
   * @param value the double value to apply the constraint to.
   * @return the constrained double value.
   */
  fun constraint(value: Double): Double
}

/**
 * Does *not* apply any constraint
 */
object Unconstrained : NumberConstraint {
  override fun constraint(value: Int): Int {
    return value
  }

  override fun constraint(value: Double): Double {
    return value
  }
}

/**
 * Returns at least zero
 */
object ZeroOrPositive : NumberConstraint {
  override fun constraint(value: Int): Int {
    return value.coerceAtLeast(0)
  }

  override fun constraint(value: Double): Double {
    return value.coerceAtLeast(0.0)
  }
}

/**
 * Keeps a number within the provided values
 */
class CoerceIn(
  val lowerLimit: @Inclusive Int,
  val upperLimit: @Inclusive Int,
) : NumberConstraint {
  init {
    require(lowerLimit <= upperLimit) {
      "Lower constraint $lowerLimit needs to be lower (or the same) as that upper constraint $upperLimit but was not!"
    }
  }

  override fun constraint(value: Int): Int {
    return value.coerceIn(lowerLimit, upperLimit)
  }

  override fun constraint(value: Double): Double {
    return value.coerceIn(lowerLimit.toDouble(), upperLimit.toDouble())
  }
}
