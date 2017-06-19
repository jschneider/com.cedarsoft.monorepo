/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.concurrent;

import com.cedarsoft.exceptions.CanceledException;
import com.cedarsoft.unit.si.ms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Timer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Keeps references to the threads that have been created
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AccountingThreadService implements ThreadService {
  @Nonnull
  private final List<WeakReference<Thread>> threads = Collections.synchronizedList(new ArrayList<>());
  @Nonnull
  private final List<WeakReference<ExecutorService>> executorServices = Collections.synchronizedList(new ArrayList<>());
  @Nonnull
  private final List<WeakReference<Timer>> swingTimers = Collections.synchronizedList(new ArrayList<>());
  @Nonnull
  private final List<WeakReference<java.util.Timer>> timers = Collections.synchronizedList(new ArrayList<>());

  /**
   * The thread factory that is used to create the threads
   */
  @Nonnull
  private final ThreadFactory threadFactory;

  @Nonnull
  private final AtomicBoolean isShutdown = new AtomicBoolean();

  public AccountingThreadService(@Nonnull ThreadFactory threadFactory) {
    this.threadFactory = threadFactory;
  }

  @Nonnull
  @Override
  public Thread newThread(@Nonnull Runnable r) {
    ensureNotShutdown();

    Thread thread = threadFactory.newThread(r);
    threads.add(new WeakReference<>(thread));
    return thread;
  }

  @Override
  @Nonnull
  public ExecutorService newFixedThreadPool(int nThreads, @Nonnull String name) {
    ensureNotShutdown();

    ExecutorService executorService = Executors.newFixedThreadPool(nThreads, new NamedThreadFactory(this, name));
    executorServices.add(new WeakReference<>(executorService));
    return executorService;
  }

  @Override
  @Nonnull
  public ExecutorService newCachedThreadPool(@Nonnull String name) {
    ensureNotShutdown();

    ExecutorService executorService = Executors.newCachedThreadPool(new NamedThreadFactory(this, name));
    executorServices.add(new WeakReference<>(executorService));
    return executorService;
  }

  @Nonnull
  @Override
  public ScheduledExecutorService newScheduledThreadPool(int nThreads, @Nonnull String name) {
    ensureNotShutdown();

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(nThreads, new NamedThreadFactory(this, name));
    executorServices.add(new WeakReference<>(executor));
    return executor;
  }

  @Nonnull
  @Override
  public java.util.Timer newTimer(@Nonnull String name) {
    java.util.Timer timer = new java.util.Timer();
    timers.add(new WeakReference<>(timer));
    return timer;
  }

  @Override
  @Nonnull
  public Timer newSwingTimer(@ms int delay) {
    Timer swingTimer = new Timer(delay, null);
    swingTimers.add(new WeakReference<>(swingTimer));
    return swingTimer;
  }

  @Nonnull
  public final List<? extends WeakReference<Thread>> getThreads() {
    return Collections.unmodifiableList(threads);
  }

  @Nonnull
  public List<? extends WeakReference<ExecutorService>> getExecutorServices() {
    return Collections.unmodifiableList(executorServices);
  }

  private void ensureNotShutdown() {
    if (isShutdown.get()) {
      throw new CanceledException();
    }
  }

  public void shutdown() {
    isShutdown.set(true);

    //interrupt all threads
    for (WeakReference<Thread> threadRef : threads) {
      @Nullable Thread thread = threadRef.get();
      if (thread != null) {
        thread.interrupt();
      }
    }

    for (WeakReference<ExecutorService> executorService : executorServices) {
      ExecutorService service = executorService.get();
      if (service != null) {
        service.shutdownNow();
      }
    }

    //close all timers
    for (WeakReference<Timer> timerReference : swingTimers) {
      Timer timer = timerReference.get();
      if (timer != null) {
        timer.stop();
      }
    }

    for (WeakReference<java.util.Timer> timerReference : timers) {
      java.util.Timer timer = timerReference.get();
      if (timer != null) {
        timer.cancel();
      }
    }
  }

  public boolean awaitTermination(int timeout, TimeUnit timeUnit) throws InterruptedException {
    boolean success = true;
    for (WeakReference<ExecutorService> executorService : executorServices) {
      ExecutorService service = executorService.get();
      if (service != null) {
        if (!service.awaitTermination(timeout, timeUnit)) {
          success = false;
        }
      }
    }
    return success;
  }
}
