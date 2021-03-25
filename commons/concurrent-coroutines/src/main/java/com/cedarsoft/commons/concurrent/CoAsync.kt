package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch

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
   * Starts the async
   */
  fun start(scope: CoroutineScope): CoAsync {
    scope.launch {
      val subscription = channel.openSubscription()

      while (!subscription.isClosedForReceive) {
        val runnable = subscription.receive()
        runnable()
      }
    }

    return this
  }

  suspend fun last(runnable: suspend () -> Unit) {
    channel.send(runnable)
  }
}
