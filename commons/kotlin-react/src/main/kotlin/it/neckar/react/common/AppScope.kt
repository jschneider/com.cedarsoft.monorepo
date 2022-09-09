package it.neckar.react.common

import it.neckar.commons.kotlin.js.exception.errorHandler
import kotlinx.coroutines.*

/**
 * App Scope - should be used as base scope
 */
val AppScope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
  errorHandler.error(throwable)
})
