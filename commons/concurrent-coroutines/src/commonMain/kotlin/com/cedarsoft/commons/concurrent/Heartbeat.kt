package com.cedarsoft.commons.concurrent

import com.cedarsoft.unit.si.ms
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.jvm.JvmInline

/**
 * Checks the heartbeat
 */
class Heartbeat(
  /**
   * The delay between two checks
   */
  val delayBetweenChecks: @ms Long = 5000,
  /**
   * The timeout for each check. If the check takes longer than this
   * timeout, it is cancelled and [Dead] emitted.
   */
  val timeout: @ms Long = 500,
  /**
   * Will be called regularly.
   * Returns the HeartbeatState
   */
  val checkConnection: suspend () -> HeartbeatState
) {

  /**
   * Starts the heart beat.
   *
   * Returns a flow with the heartbeat results
   */
  fun runHeartBeat(): Flow<HeartbeatState> {
    return flow {
      while (currentCoroutineContext().isActive) {
        //Run the first check immediately
        val result = withTimeoutOrNull(timeout) {
          try {
            emit(checkConnection.invoke())
          } catch (e: CancellationException) {
            //has been cancelled, rethrow the exception
            throw e
          } catch (e: Throwable) {
            //Some exception has occurred, connection is dead
            emit(ExceptionOccurred(e))
          }
        }

        //Timeout reached
        if (result == null) {
          emit(TimedOut)
        }

        //delay
        delay(delayBetweenChecks)
      }
    }
  }
}

/**
 * The heartbeat state
 */
sealed interface HeartbeatState {
  /**
   * Returns true if this is a success
   */
  val isSuccess: Boolean
    get() {
      return this is Alive
    }
}

/**
 * The connection is alive
 */
@JvmInline
value class Alive(val latency: @ms Double) : HeartbeatState {
}

/**
 * No response - the connection is dead
 */
abstract class Dead : HeartbeatState {
}

/**
 * The connection has timed out
 */
object TimedOut : Dead() {
}

/**
 * The response was invalid
 */
object InvalidResponse : Dead() {
}

/**
 * An exception has occurred
 */
data class ExceptionOccurred(val exception: Throwable) : Dead() {
}
