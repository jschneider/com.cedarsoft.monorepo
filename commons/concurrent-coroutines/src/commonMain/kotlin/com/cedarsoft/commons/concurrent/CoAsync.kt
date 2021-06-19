package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**
 * Async implementation backed by coroutines
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class CoAsync {
  /**
   * The channel that holds the runnables that are executed
   */
  private val channel = ConflatedBroadcastChannel<suspend () -> Unit>()

  /**
   * The job that is created on launch
   */
  private var launch: Job? = null

  /**
   * Starts the async
   */
  fun start(scope: CoroutineScope): Job {
    return scope.launch() {
      val subscription = channel.openSubscription()

      while (!subscription.isClosedForReceive) {
        val runnable = subscription.receive()

        //Launch in
        launch(Dispatchers.Unconfined + SupervisorJob()) {
          runnable()
        }.join()
      }
    }.also {
      this.launch = it
    }
  }

  suspend fun dispose() {
    launch?.cancelAndJoin()
  }

  suspend fun last(runnable: suspend () -> Unit) {
    channel.send(runnable)
  }
}
