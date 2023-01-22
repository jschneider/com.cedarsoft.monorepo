package it.neckar.react.common.rest

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlagsSupport
import it.neckar.react.common.*
import it.neckar.react.common.toast.*
import kotlinx.coroutines.*
import react.*

/**
 * Executes a REST action.
 * Returns immediately.
 *
 * Helper methods that does not return anything.
 * Use [restAsync] if the return type shall be used.
 */
fun <T> rest(successMessage: String? = null, action: suspend () -> T) {
  AppScope.launch {
    restBlocking(successMessage, action)
  }
}

/**
 * Executes a REST action.
 * Returns immediately.
 *
 * To avoid warnings if the returned value is not used, call [rest] instead.
 *
 * ATTENTION: Use the return type!
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

/**
 * Executes an asynchronous rest call.
 * Sets the busy state to true - and reverts it after the call has finished.
 *
 * This method should be used for buttons that show some kind of busy indicator while the provided action is executed
 */
fun <T> restBusy(busy: StateSetter<Boolean>, successMessage: String? = null, action: suspend () -> T) {
  busy(true)
  rest(successMessage) {
    try {
      action()
    } finally {
      busy(false)
    }
  }
}
