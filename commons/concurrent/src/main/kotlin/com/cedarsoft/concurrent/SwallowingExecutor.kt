package com.cedarsoft.concurrent

import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException

/**
 * A [Executor] that swallows [RejectedExecutionException] exceptions.
 */
class SwallowingExecutor(private val executor: Executor) : Executor {
  override fun execute(command: Runnable) {
    try {
      executor.execute(command)
    } catch (ignored: RejectedExecutionException) {
      // ignored
    }
  }

  companion object {
    @JvmStatic
    fun wrap(executor: Executor): Executor {
      return SwallowingExecutor(executor)
    }
  }
}
