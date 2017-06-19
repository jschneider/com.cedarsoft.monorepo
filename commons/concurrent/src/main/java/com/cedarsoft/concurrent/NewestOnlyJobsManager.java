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
