package it.neckar.react.common.rest

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlagsSupport
import it.neckar.react.common.*
import it.neckar.react.common.toast.*
import kotlinx.coroutines.*

/**
 * Executes a REST action.
 * Returns immediately.
 *
 * Helper methods that does not return anything.
 * Use [restAsync] if the return type shall be used.
 */
fun <T> rest(successMessage: String? = null, action: suspend () -> T) {
  @Suppress("DeferredResultUnused")
  restAsync(successMessage, action)
}

/**
 * Executes a REST action.
 * Returns immediately.
 *
 * To avoid warnings if the returned value is not used, call [rest] instead.
 */
fun <T> restAsync(successMessage: String? = null, action: suspend () -> T): Deferred<T> {
  return AppScope.async {
    restBlocking(successMessage, action)
  }
}

/**
 * Executes a REST action within the scope.
 * Suspend function that blocks
 */
suspend fun <T> restBlocking(successMessage: String?, action: suspend () -> T): T {
  delayIfSlowUiEnabled()
  val result = action()
  if (successMessage != null) {
    Toast.info(
      successMessage, options = ToastOptions(
        timeOut = 2000,
        positionClass = ToastPosition.BOTTOMCENTER
      )
    )
  }

  return result
}

/**
 * This method delays the UI if the corresponding feature flag is enabled
 */
suspend fun delayIfSlowUiEnabled() {
  if (FeatureFlagsSupport.flags.contains(FeatureFlag.slowUi)) {
    delay(1000)
  }
}
