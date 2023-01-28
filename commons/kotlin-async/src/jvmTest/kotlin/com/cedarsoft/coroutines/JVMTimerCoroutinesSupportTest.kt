package com.cedarsoft.coroutines

import com.cedarsoft.time.JVMTimerCoroutineSupport
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
