package it.neckar.open.concurrent

import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.concurrent.GuardedBy
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Helper class that is guarded by lock.
 * Contains a mutable property
 */
class GuardedProperty<T>(
  val lock: ReentrantReadWriteLock,

  /**
   * The current property (that is mutable!)
   */
  @GuardedBy("lock")
  private var property: T
) {

  /**
   * Convenience constructor that instantiates a new ReentrantReadWriteLock
   */
  constructor(state: T) : this(ReentrantReadWriteLock(), state)

  inline fun <Y> read(action: GuardedProperty<T>.() -> Y): Y = lock.read { this.action() }

  inline fun <Y> write(action: GuardedProperty<T>.() -> Y): Y = lock.write { this.action() }

  /**
   * Returns the current property
   */
  fun get(): T {
    read {
      return property
    }
  }

  /**
   * Set the new property
   */
  fun set(newState: T) {
    write {
      property = newState
    }
  }
}
