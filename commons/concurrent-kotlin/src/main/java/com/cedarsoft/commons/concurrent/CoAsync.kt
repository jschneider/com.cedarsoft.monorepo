package com.cedarsoft.commons.concurrent

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**
 * Async implementation backed by coroutines
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@ExperimentalCoroutinesApi
class CoAsync {

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
