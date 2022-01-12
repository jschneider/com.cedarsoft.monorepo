package it.neckar.react.common

import kotlinx.coroutines.*

/**
 * Scope that can/should be used for heartbeat checks
 */
val HeartbeatScope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
  println("Heartbeat check failed with a throwable: $throwable")
})
