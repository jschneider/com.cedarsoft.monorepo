package it.neckar.problem

import com.cedarsoft.exceptions.ApplicationException
import com.cedarsoft.exceptions.ErrorCode
import com.cedarsoft.i18n.I18nConfiguration
import it.neckar.problem.io.AnySerializer
import kotlinx.serialization.Serializable

/**
 * Represents an (expected) problem.
 *
 * All strings are in english.
 * use the [errorCode] for translation on the client side.
 */
@Serializable
data class ExpectedProblem(
  /**
   * The error code for this problem
   */
  val errorCode: ErrorCode,

  /**
   * A short, human-readable summary of the problem type.
   */
  val title: String?,

  /**
   * The status type
   */
  val status: Status,

  /**
   * The message
   */
  override val message: String?,

  override val instanceUri: String?,

  /**
   * Optional, additional attributes
   */
  val attributes: Map<String, @Serializable(with = AnySerializer::class) Any?>?,

  override val stackTrace: String?
) : Problem

/**
 * Converts an application problem to an expected problem
 */
fun ApplicationException.toExpectedProblem(
  status: Status = Status.BAD_REQUEST,
  i18nConfiguration: I18nConfiguration
): ExpectedProblem {
  return ExpectedProblem(
    errorCode = errorCode,
    title = getTitle(i18nConfiguration),
    status = status,
    message = getLocalizedMessage(i18nConfiguration),
    instanceUri = null,
    attributes = this.parameters,
    stackTrace = this.stackTraceToString()
  )
}

@Deprecated("Convert to expected problem instead", level = DeprecationLevel.ERROR)
fun ApplicationException.toUnexpectedProblem(): UnexpectedProblem {
  throw UnsupportedOperationException("Must not convert to UnexpectedProblem.")
}
