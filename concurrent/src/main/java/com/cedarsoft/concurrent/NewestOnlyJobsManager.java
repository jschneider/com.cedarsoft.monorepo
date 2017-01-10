package com.cedarsoft.concurrent;

import com.cedarsoft.annotations.Blocking;
import com.cedarsoft.annotations.NonUiThread;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Job manager that only executes the newest job for a key.
 * Old jobs are automatically discarded.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ThreadSafe
public interface NewestOnlyJobsManager {
  /**
   * Schedules a new job.
   */
  void scheduleJob(@Nonnull Job job);

  /**
   * Clears all jobs for the given key
   */
  void clearJobs(@Nonnull Object key);

  /**
   * Represents a job.
   * Each job has a key - these are compared using #equals().
   * Only the newest job for every key is executed.
   */
  interface Job {
    /**
     * The key for the job. Only the youngest job for a given key is executed
     */
    @Nonnull
    Object getKey();

    /**
     * Executes the job
     */
    @NonUiThread
    @Blocking
    void execute();
  }
}
