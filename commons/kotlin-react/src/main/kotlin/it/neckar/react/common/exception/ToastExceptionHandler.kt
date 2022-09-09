package it.neckar.react.common.exception

import com.cedarsoft.exceptions.PermissionDeniedException
import it.neckar.commons.kotlin.js.exception.ConsoleJsErrorHandler
import it.neckar.commons.kotlin.js.exception.JsErrorHandler
import it.neckar.commons.kotlin.js.exception.JsErrorHandlerMultiplexer
import it.neckar.commons.kotlin.js.exception.errorHandler
import it.neckar.commons.kotlin.js.exception.isFailToFetch
import it.neckar.react.common.toast.*

/**
 * Handles exceptions by showing a toast message
 */
class ToastExceptionHandler(
  val formatter: ExceptionFormatter = DefaultExceptionFormatter,
) : JsErrorHandler {
  override fun otherError(message: dynamic, error: Any) {
    Toast.error(
      "Ein Fehler ist aufgetreten:<br>${message}", "Fehler", ToastOptions(
        timeOut = 8000,
        positionClass = ToastPosition.BOTTOMCENTER,
      )
    )
  }

  override fun error(throwable: Throwable) {
    val info = formatter.format(throwable) ?: fallbackFormat(throwable)

    when (info.severity) {
      TranslatedExceptionInfo.Severity.Info -> Toast.info(info.message, info.title, ToastOptions(positionClass = info.toastPosition, timeOut = 5000))
      TranslatedExceptionInfo.Severity.Success -> Toast.success(info.message, info.title, ToastOptions(positionClass = info.toastPosition, timeOut = 5000))
      TranslatedExceptionInfo.Severity.Warning -> Toast.warning(info.message, info.title, ToastOptions(positionClass = info.toastPosition, timeOut = 5000))
      TranslatedExceptionInfo.Severity.Error -> Toast.error(info.message, info.title, ToastOptions(positionClass = info.toastPosition, timeOut = 5000))
    }
  }

  fun fallbackFormat(throwable: Throwable): TranslatedExceptionInfo {
    console.info("Falling back to default error handling for <${throwable::class.simpleName}>")
    return TranslatedExceptionInfo("Fehler", "Ein Fehler ist aufgetreten:<br>${throwable.message}", TranslatedExceptionInfo.Severity.Error)
  }

  companion object {
    /**
     * Installs this exception handler
     */
    fun install(formatter: ExceptionFormatter = DefaultExceptionFormatter) {
      JsErrorHandler.registerWindowErrorHandler()
      errorHandler = JsErrorHandlerMultiplexer(listOf(ConsoleJsErrorHandler, ToastExceptionHandler(formatter)))
    }
  }
}

/**
 * Translates / formats exceptions
 */
interface ExceptionFormatter {
  /**
   * Returns the translation for the given [throwable].
   * If null is returned a fallback formatting will be used
   */
  fun format(throwable: Throwable): TranslatedExceptionInfo?
}

/**
 * Contains information about an error that can be shown in a toast
 */
data class TranslatedExceptionInfo(
  val title: String,
  val message: String,
  val severity: Severity,
  /**
   * The position for the position
   */
  val toastPosition: ToastPosition = ToastPosition.BOTTOMCENTER,
) {

  /**
   * The severity of the message
   */
  enum class Severity {
    Info,
    Success,
    Warning,
    Error
  }
}

/**
 * Default implement for exception formatter
 */
object DefaultExceptionFormatter : ExceptionFormatter {
  override fun format(throwable: Throwable): TranslatedExceptionInfo? {
    if (throwable.isFailToFetch()) {
      return TranslatedExceptionInfo("Netzwerkfehler", "Der Server ist aktuell nicht erreichbar", TranslatedExceptionInfo.Severity.Error)
    }

    if (throwable is PermissionDeniedException) {
      return TranslatedExceptionInfo("Zugriff nicht erlaubt", "Der Zugriff wurde verweigert.", TranslatedExceptionInfo.Severity.Error)
    }

    return null
  }
}
