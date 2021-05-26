/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package com.cedarsoft.concurrent

import java.io.StringWriter
import java.io.Writer
import java.lang.management.ManagementFactory
import java.lang.management.ThreadInfo
import java.util.Arrays

/**
 * Dumps threads
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
object ThreadDumper {

  //Fallback if the other method does not work
  @JvmStatic
  val threadInfos: Array<ThreadInfo>
    get() {
      val threadMXBean = ManagementFactory.getThreadMXBean()

      try {
        return sort(threadMXBean.dumpAllThreads(false, false))
      } catch (ignored: Exception) {
      }

      val threadIds = threadMXBean.allThreadIds
      return sort(threadMXBean.getThreadInfo(threadIds, Integer.MAX_VALUE))
    }

  @JvmStatic
  fun dumpThreads(threadInfos: Array<ThreadInfo>): String {
    val writer = StringWriter()
    dumpThreads(threadInfos, writer)
    return writer.toString()
  }

  /**
   * Dumps the threads. Returns the stack trace of the EDT
   */
  @JvmStatic
  fun dumpThreads(threadInfos: Array<ThreadInfo>, writer: Writer): Array<StackTraceElement>? {
    var edtStack: Array<StackTraceElement>? = null

    for (info in threadInfos) {
      if (info.threadName.startsWith("AWT-EventQueue-")) {
        edtStack = info.stackTrace
      }
      dumpThreadInfo(info, writer)
    }

    return edtStack
  }

  @JvmStatic
  fun dumpThreads(writer: Writer): Array<StackTraceElement>? {
    return dumpThreads(threadInfos, writer)
  }

  private fun sort(threads: Array<ThreadInfo>): Array<ThreadInfo> {
    Arrays.sort(threads, fun(o1: ThreadInfo, o2: ThreadInfo): Int {
      val t1 = o1.threadName
      val t2 = o2.threadName
      if (t1.startsWith("AWT-EventQueue")) {
        return -1
      }
      if (t2.startsWith("AWT-EventQueue")) {
        return 1
      }
      val r1 = o1.threadState == Thread.State.RUNNABLE
      val r2 = o2.threadState == Thread.State.RUNNABLE
      if (r1 && !r2) {
        return -1
      }
      if (r2 && !r1) {
        return 1
      }
      return 0
    })

    return threads
  }

  @JvmStatic
  fun dumpThreadInfo(info: ThreadInfo, writer: Writer) {
    dumpCallStack(info, writer, info.stackTrace)
  }

  private fun dumpCallStack(info: ThreadInfo, writer: Writer, stackTraceElements: Array<StackTraceElement>) {
    val sb = StringBuilder("\"").append(info.threadName).append("\"")
    sb.append(" prio=0 tid=0x0 nid=0x0 ").append(getReadableState(info.threadState)).append("\n")
    sb.append("     java.lang.Thread.State: ").append(info.threadState).append("\n")
    if (info.lockName != null) {
      sb.append(" on ").append(info.lockName)
    }
    if (info.lockOwnerName != null) {
      sb.append(" owned by \"").append(info.lockOwnerName).append("\" Id=").append(info.lockOwnerId)
    }
    if (info.isSuspended) {
      sb.append(" (suspended)")
    }
    if (info.isInNative) {
      sb.append(" (in native)")
    }

    writer.write(sb.toString() + "\n")
    for (element in stackTraceElements) {
      writer.write("\tat $element\n")
    }
    writer.write("\n")
  }

  private fun getReadableState(state: Thread.State): String {
    when (state) {
      Thread.State.BLOCKED                             -> return "blocked"
      Thread.State.TIMED_WAITING, Thread.State.WAITING -> return "waiting on condition"
      Thread.State.RUNNABLE                            -> return "runnable"
      Thread.State.NEW                                 -> return "new"
      Thread.State.TERMINATED                          -> return "terminated"
    }
  }
}
/**
 * Creates a string containing all thread dumps
 */
