package it.neckar.react.common.rest

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlagsSupport
import it.neckar.react.common.*
import kotlinx.coroutines.*
import toast.Toast
import toast.ToastOptions
import toast.ToastPosition

/**
 *
 */

/**
 * Executes a REST action.
 * Returns immediately.
 */
fun rest(successMessage: String? = null, action: suspend () -> dynamic) {
  AppScope.launch {
    restBlocking(successMessage, action)
  }
}

/**
 * Executes a REST action within the scope.
 * Suspend function that blocks
 */
suspend fun restBlocking(successMessage: String?, action: suspend () -> dynamic) {
  delayIfSlowUiEnabled()
  action()
  if (successMessage != null) {
    Toast.info(
      successMessage, options = ToastOptions(
        timeOut = 2000,
        positionClass = ToastPosition.BOTTOMCENTER
      )
    )
  }
}

/**
 * This method delays the UI if the corresponding feature flag is enabled
 */
suspend fun delayIfSlowUiEnabled() {
  if (FeatureFlagsSupport.flags.contains(FeatureFlag.slowUi)) {
    delay(1000)
  }
}
