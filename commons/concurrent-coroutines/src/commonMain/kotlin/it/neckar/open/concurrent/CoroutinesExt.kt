package it.neckar.open.concurrent

/**
 * Extension methods related to coroutines
 */
import kotlinx.coroutines.*


/**
 * Basic debounce operators based on [the Stack Overflow answer](https://stackoverflow.com/a/55802898/5102726) from [Patrick](https://stackoverflow.com/users/751581/patrick)
 *
 * Usage:
 * ```
 * val onEmailChange: (String) -> Unit = throttleLatest(300L, viewLifecycleOwner.lifecycleScope, viewModel::onEmailChanged)
 * emailTextView.onTextChanged(onEmailChange)
 * ```
 */

/**
 * Constructs a function that processes input data and passes the latest data to [destinationFunction] in the end of every time window with length of [intervalMs].
 */
fun <T> throttleLatest(
  intervalMs: Long = 300L,
  coroutineScope: CoroutineScope,
  destinationFunction: (T) -> Unit
): (T) -> Unit {
  var throttleJob: Job? = null
  var latestParam: T
  return { param: T ->
    latestParam = param
    if (throttleJob?.isCompleted != false) {
      throttleJob = coroutineScope.launch {
        delay(intervalMs)
        destinationFunction(latestParam)
      }
    }
  }
}


/**
 * Constructs a function that processes input data and passes it to [destinationFunction] only if there's no new data for at least [waitMs]
 */
fun <T> debounce(
  waitMs: Long = 300L,
  coroutineScope: CoroutineScope,
  destinationFunction: (T) -> Unit
): (T) -> Unit {
  var debounceJob: Job? = null
  return { param: T ->
    debounceJob?.cancel()
    debounceJob = coroutineScope.launch {
      delay(waitMs)
      destinationFunction(param)
    }
  }
}


/**
 * Constructs a function that processes input data and passes the first data to [destinationFunction] and skips all new data for the next [skipMs].
 */
fun <T> throttleFirst(
  skipMs: Long = 300L,
  coroutineScope: CoroutineScope,
  destinationFunction: (T) -> Unit
): (T) -> Unit {
  var throttleJob: Job? = null
  return { param: T ->
    if (throttleJob?.isCompleted != false) {
      throttleJob = coroutineScope.launch {
        destinationFunction(param)
        delay(skipMs)
      }
    }
  }
}
