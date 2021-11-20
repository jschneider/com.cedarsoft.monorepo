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

class CustomIntegerConstraint(
  private val lowerConstraint: Int,
  private val upperConstraint: Int,
) : NumberConstraint {
  init {
    require(lowerConstraint < upperConstraint) {
      "Lower constraint $lowerConstraint needs to be lower that upper constraint $upperConstraint but was not!"
    }
  }

  override fun constraint(value: Int): Int {
    return value.coerceIn(lowerConstraint, upperConstraint)
  }

  override fun constraint(value: Double): Double {
    return constraint(value.toInt()).toDouble()
  }
}
