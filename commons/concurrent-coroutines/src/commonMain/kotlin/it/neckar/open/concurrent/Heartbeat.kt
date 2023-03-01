package it.neckar.open.concurrent

import it.neckar.open.unit.si.ms
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
  val timeout: @ms Long = 1000,
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
          emit(ConnectionFailure)
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
  val isServiceAvailable: Boolean
    get() = this is Alive

  /**
   * Returns true if there is a version mismatch
   */
  val isVersionMismatch: Boolean
    get() = this == VersionMismatch
}

/**
 * The connection is alive
 */
@JvmInline
value class Alive(val latency: @ms Double) : HeartbeatState {
  override fun toString(): String {
    return "Alive(latency=$latency)"
  }
}

/**
 * No response - the connection is dead
 */
abstract class Dead : HeartbeatState

/**
 * Connection failed (or timed out)
 */
object ConnectionFailure : Dead() {
  override fun toString(): String {
    return "ConnectionFailure"
  }
}

/**
 * The client/server version do not match. The client should be updated.
 */
object VersionMismatch : Dead() {
  override fun toString(): String {
    return "VersionMismatch"
  }
}

/**
 * The server responded with a non success status code (>=400)
 */
data class ErrorResponse(val status: Int, val message: String?) : Dead()

/**
 * An exception has occurred
 */
data class ExceptionOccurred(val exception: Throwable) : Dead()

