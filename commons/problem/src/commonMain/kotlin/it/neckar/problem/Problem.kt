package it.neckar.problem

/**
 * The problem
 */
interface Problem {
  /**
   * The message of the problem
   */
  val message: String?

  /**
   * URI to the concrete instance of the specific problem
   */
  val instanceUri: String?

  /**
   * The optional stack trace elements
   */
  val stackTrace: String?
}
