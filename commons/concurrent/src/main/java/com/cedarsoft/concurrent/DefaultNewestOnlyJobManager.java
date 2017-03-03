package com.cedarsoft.concurrent;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation for newest only jobs manager
 */
@ThreadSafe
public class DefaultNewestOnlyJobManager implements NewestOnlyJobsManager {
  private static final Logger LOG = Logger.getLogger(DefaultNewestOnlyJobManager.class.getName());

  @Nonnull
  private final ExecutorService executorService;
  private final int workerCount;
  /**
   * The max amount of jobs that are held
   */
  private final int maxJobsCount;

  /**
   * Contains the tile identifiers that shall be calculated
   */
  @Nonnull
  private final BlockingDeque<Job> jobs;

  public DefaultNewestOnlyJobManager(@Nonnull ExecutorService executorService, int workerCount) {
    this(executorService, workerCount, 50);
  }

  public DefaultNewestOnlyJobManager(@Nonnull ExecutorService executorService, int workerCount, int maxJobsCount) {
    this.executorService = executorService;
    this.workerCount = workerCount;
    this.maxJobsCount = maxJobsCount;
    this.jobs = new LinkedBlockingDeque<>(maxJobsCount);

    if (workerCount < 1) {
      throw new IllegalArgumentException("Need at least one worker but was <" + workerCount + ">");
    }
  }

  /**
   * Starts the workers
   */
  @PostConstruct
  public void startWorkers() {
    //Start the worker threads
    for (int i = 0; i < workerCount; i++) {
      executorService.execute(new Worker());
    }
  }

  /**
   * A new job is scheduled
   */
  @Override
  @UiThread
  @NonUiThread
  public void scheduleJob(@Nonnull Job job) {
    if (LOG.isLoggable(Level.FINE)) {
      LOG.fine("schedule job for key <" + job.getKey() + ">. Old size <" + jobs.size() + ">");
    }
    //Remove old jobs with the same key
    clearJobs(job.getKey());

    //Schedule for calculation
    while (!jobs.offerFirst(job)) {
      //Cleanup if necessary
      ensureMaxJobsSize();
    }
  }

  @Override
  public void clearJobs(@Nonnull Object key) {
    jobs.removeIf(job -> job.getKey().equals(key));
  }

  /**
   * Deletes old jobs to ensure the max job count is respected
   */
  private void ensureMaxJobsSize() {
    if (LOG.isLoggable(Level.FINE)) {
      LOG.fine("Cleaning up old jobs <" + jobs.size() + ">");
    }
    while (jobs.size() >= maxJobsCount) {
      jobs.pollLast();
    }
  }

  /**
   * Returns the next job from the queue
   */
  @Nonnull
  private Job getNextJob() throws InterruptedException {
    Job job = jobs.takeFirst();
    //Clean up all other jobs with the given key to avoid duplicate actions
    clearJobs(job.getKey());
    return job;
  }

  /**
   * Creates a new instance with a worker count depending on the available processors count.
   * <p>
   * Creates a manager with (processors-count - 1) workers.
   */
  @Nonnull
  public static DefaultNewestOnlyJobManager create() {
    //Calculate the amount of background painting threads
    int cores = Runtime.getRuntime().availableProcessors();
    int workerCount = Math.max(1, cores - 1);

    return new DefaultNewestOnlyJobManager(Executors.newFixedThreadPool(workerCount, new NamedThreadFactory("GlobalOptionalJobManager")), cores);
  }

  /**
   * Worker that always gets the first job and solves it
   */
  private class Worker implements Runnable {
    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          getNextJob().execute();
        } catch (InterruptedException ignore) {
          return;
        } catch (Throwable e) {
          Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
        }
      }
    }

  }
}