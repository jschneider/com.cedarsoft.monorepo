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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

/**
 * Dumps threads
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ThreadDumper {
  private ThreadDumper() {
  }

  /**
   * Creates a string containing all thread dumps
   */
  @Nonnull
  public static String dumpThreads() {
    return dumpThreads(getThreadInfos());
  }

  @Nonnull
  public static String dumpThreads(@Nonnull ThreadInfo[] threadInfos) {
    try {
      StringWriter writer = new StringWriter();
      dumpThreads(threadInfos, writer);
      return writer.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Dumps the threads. Returns the stack trace of the EDT
   */
  @Nullable
  public static StackTraceElement[] dumpThreads(@Nonnull final ThreadInfo[] threadInfos, @Nonnull final Writer writer) throws IOException {
    StackTraceElement[] edtStack = null;

    for (ThreadInfo info : threadInfos) {
      if (info != null) {
        if (info.getThreadName().startsWith("AWT-EventQueue-")) {
          edtStack = info.getStackTrace();
        }
        dumpThreadInfo(info, writer);
      }
    }

    return edtStack;
  }

  @Nullable
  public static StackTraceElement[] dumpThreads(@Nonnull final Writer writer) throws IOException {
    return dumpThreads(getThreadInfos(), writer);
  }

  @Nonnull
  public static ThreadInfo[] getThreadInfos() {
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    try {
      return sort(threadMXBean.dumpAllThreads(false, false));
    } catch (Exception ignored) {
    }

    //Fallback if the other method does not work
    final long[] threadIds = threadMXBean.getAllThreadIds();
    return sort(threadMXBean.getThreadInfo(threadIds, Integer.MAX_VALUE));
  }

  @Nonnull
  private static ThreadInfo[] sort(@Nonnull ThreadInfo[] threads) {
    Arrays.sort(threads, (o1, o2) -> {
      final String t1 = o1.getThreadName();
      final String t2 = o2.getThreadName();
      if (t1.startsWith("AWT-EventQueue")) {
        return -1;
      }
      if (t2.startsWith("AWT-EventQueue")) {
        return 1;
      }
      final boolean r1 = o1.getThreadState() == Thread.State.RUNNABLE;
      final boolean r2 = o2.getThreadState() == Thread.State.RUNNABLE;
      if (r1 && !r2) {
        return -1;
      }
      if (r2 && !r1) {
        return 1;
      }
      return 0;
    });

    return threads;
  }

  public static void dumpThreadInfo(@Nonnull final ThreadInfo info, @Nonnull final Writer writer) throws IOException {
    dumpCallStack(info, writer, info.getStackTrace());
  }

  private static void dumpCallStack(@Nonnull final ThreadInfo info, @Nonnull final Writer writer, @Nonnull final StackTraceElement[] stackTraceElements) throws IOException {
    StringBuilder sb = new StringBuilder("\"").append(info.getThreadName()).append("\"");
    sb.append(" prio=0 tid=0x0 nid=0x0 ").append(getReadableState(info.getThreadState())).append("\n");
    sb.append("     java.lang.Thread.State: ").append(info.getThreadState()).append("\n");
    if (info.getLockName() != null) {
      sb.append(" on ").append(info.getLockName());
    }
    if (info.getLockOwnerName() != null) {
      sb.append(" owned by \"").append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
    }
    if (info.isSuspended()) {
      sb.append(" (suspended)");
    }
    if (info.isInNative()) {
      sb.append(" (in native)");
    }

    writer.write(sb + "\n");
    for (StackTraceElement element : stackTraceElements) {
      writer.write("\tat " + element.toString() + "\n");
    }
    writer.write("\n");
  }

  @Nonnull
  private static String getReadableState(@Nonnull Thread.State state) {
    switch (state) {
      case BLOCKED:
        return "blocked";
      case TIMED_WAITING:
      case WAITING:
        return "waiting on condition";
      case RUNNABLE:
        return "runnable";
      case NEW:
        return "new";
      case TERMINATED:
        return "terminated";
    }
    throw new IllegalArgumentException("Invalid thread state <" + state + ">");
  }
}
