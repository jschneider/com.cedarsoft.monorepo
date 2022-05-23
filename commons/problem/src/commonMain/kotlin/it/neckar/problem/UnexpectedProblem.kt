package it.neckar.problem

import kotlinx.serialization.Serializable

/**
 * Represents an unexpected problem
 */
@Serializable
data class UnexpectedProblem(
  /**
   * The message from the exception
   */
  override val message: String?,
  /**
   * The class name of the exception type
   */
  val exceptionType: String,
  /**
   * The (optional instance URI)
   */
  override val instanceUri: String?,

  override val stackTrace: String?
) : Problem
