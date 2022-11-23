package com.cedarsoft.execution

import com.cedarsoft.common.lang.Os
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object Runtimes {

  /**
   * Executes the command and returns the lines as list
   */
  fun execSimpleCommand(command: List<String>): List<String> {
    val process = ProcessBuilder(command).start()
    return process.inputStream.bufferedReader().useLines { it.toList() }
  }

  /**
   * Counts the processes for the given pattern
   */
  fun countProcesses(pattern: Regex): Int {
    val command = when {
      Os.isWindows -> listOf("tasklist", "/NH")
      Os.isLinux -> listOf("ps", "-ef")
      else -> throw UnsupportedOperationException("No implemented for OS ${Os.osName}")
    }

    return execSimpleCommand(command).count {
      it.contains(pattern)
    }
  }

  /**
   * Returns true if at least one process matching the provided pattern is running.
   */
  fun isProcessRunning(pattern: Regex): Boolean {
    return countProcesses(pattern) > 0
  }

  fun Process.formatProcessInfo(): String {
    return toHandle().formatProcessInfo()
  }

  fun ProcessHandle.formatProcessInfo(): String {
    val info = info()
    val user = info.user().orElse("")
    val pid = pid()
    val ppid = parent().orElseGet { null }?.pid()?.toString() ?: "?"
    val startTime = info.startInstant().orElse(null)
    val cpuDuration = info.totalCpuDuration()?.orElse(null)
    val cmdLine = info.commandLine().orElseGet { "" }

    return String.format("%-8s %-6d %-6s %-25s %-10s %s", user, pid, ppid, startTime ?: "", cpuDuration ?: "", cmdLine)
  }

  /**
   * Destroys the process
   */
  fun destroyProcess(process: Process, shutdownWaitTime: Duration = 10.seconds) {
    val info = process.toHandle().formatProcessInfo()

    destroyProcess(process.toHandle())

    //Wait for the process to shut down
    if (process.waitFor(shutdownWaitTime.inWholeMilliseconds, TimeUnit.MILLISECONDS)) {
      //Process ended
      return
    }

    process.destroyForcibly()
    if (process.waitFor(shutdownWaitTime.inWholeMilliseconds, TimeUnit.MILLISECONDS)) {
      return
    }

    throw IllegalStateException("Could not stop process ${process.formatProcessInfo()}")
  }

  /**
   * Destroys a process (and its child processes).
   */
  private fun destroyProcess(process: ProcessHandle) {
    process.children().forEach { destroyProcess(it) }
    process.destroy()
  }
}
