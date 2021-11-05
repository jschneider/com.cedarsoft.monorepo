package it.neckar.react.common.form

/**
 * Constraint for a number
 */
sealed interface NumberConstraint {
  fun constraint(value: Int): Int
  fun constraint(value: Double): Double
}

object Unconstraint : NumberConstraint {
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
