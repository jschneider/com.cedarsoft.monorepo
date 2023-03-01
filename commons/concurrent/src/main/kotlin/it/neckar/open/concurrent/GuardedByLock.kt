package it.neckar.open.concurrent

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Helper class that is guarded by lock.
 * Can be used to wrap mutable objects (e.g. lists)
 */
class GuardedByLock<out T>(
  val lock: ReentrantReadWriteLock,
  val state: T
) {

  /**
   * Convenience constructor that instantiates a new ReentrantReadWriteLock
   */
  constructor(state: T) : this(ReentrantReadWriteLock(), state)

  inline fun <Y> read(action: T.() -> Y): Y = lock.read { state.action() }

  inline fun <Y> write(action: T.() -> Y): Y = lock.write { state.action() }
}
