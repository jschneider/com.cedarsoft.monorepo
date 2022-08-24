package com.cedarsoft.concurrent

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import javax.annotation.Nonnull

/**
 * Provides methods that support concurrency.
 *
 */
object ConcurrentUtil {
  @JvmStatic
  fun createSingleThreadScheduledExecutor(spawnDaemonThread: Boolean, threadName: String?): ScheduledExecutorService {
    return Executors.newSingleThreadScheduledExecutor(CustomizableThreadFactory(spawnDaemonThread, threadName))
  }

  @JvmStatic
  fun createSingleThreadExecutor(spawnDaemonThread: Boolean, threadName: String?): ExecutorService {
    return Executors.newSingleThreadExecutor(CustomizableThreadFactory(spawnDaemonThread, threadName))
  }

  private class CustomizableThreadFactory(
    private val spawnDaemonThread: Boolean,
    private val threadName: String?,
  ) : ThreadFactory {
    override fun newThread(@Nonnull r: Runnable): Thread {
      val thread = Thread(r)
      thread.isDaemon = spawnDaemonThread
      if (threadName != null) {
        thread.name = threadName
      }
      return thread
    }
  }
}
