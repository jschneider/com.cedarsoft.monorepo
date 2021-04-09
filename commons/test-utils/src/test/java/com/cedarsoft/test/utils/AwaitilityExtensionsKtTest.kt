package com.cedarsoft.test.utils

import org.awaitility.Awaitility
import org.awaitility.core.ConditionTimeoutException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class AwaitilityExtensionsKtTest {
  @Test
  internal fun testIt() {
    val atomicBoolean = AtomicBoolean(true)
    Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAtomicIsTrue(atomicBoolean)
  }

  @Test
  internal fun testTimeout() {
    val atomicBoolean = AtomicBoolean(false)
    try {
      Awaitility.await().atMost(30, TimeUnit.SECONDS)
        .atMost(101, TimeUnit.MILLISECONDS)
        .untilAtomicIsTrue(atomicBoolean)

      fail("where is the exception")
    } catch (ignore: ConditionTimeoutException) {
    }
  }
}
