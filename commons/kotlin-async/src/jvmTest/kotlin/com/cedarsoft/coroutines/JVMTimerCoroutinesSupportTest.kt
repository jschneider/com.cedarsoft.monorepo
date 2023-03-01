package it.neckar.open.coroutines

import it.neckar.open.time.JVMTimerCoroutineSupport
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class JVMTimerCoroutinesSupportTest {
  @Test
  fun testRepeat() = runTest {
    val support = JVMTimerCoroutineSupport()
    support.repeat(3.seconds) { println("test") }
  }
}
