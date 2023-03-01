package it.neckar.open.execution

import it.neckar.open.execution.Runtimes.formatProcessInfo
import kotlinx.coroutines.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class RuntimesTest {
  @Test
  @Disabled
  fun testProcessInfo() = runBlocking {
    val process = ProcessBuilder("/usr/bin/google-chrome-beta").start()
    process.info()

    val formatted = process.toHandle().formatProcessInfo()
    println(formatted)

    delay(2000)

    Runtimes.destroyProcess(process, 2.seconds)
  }

  @Disabled
  @Test
  fun testCountProcesses() {
    val countSystemProcess = Runtimes.countProcesses("firefox".toRegex())
    println("countSystemProcess: $countSystemProcess")
  }
}
