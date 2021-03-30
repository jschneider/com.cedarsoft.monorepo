package com.cedarsoft.commons.concurrent

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.untilAtomicIsTrue
import kotlinx.coroutines.*
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 */
class DeferredTest {
  @Test
  fun name() = runBlocking {
    val s: Deferred<String> = CompletableDeferred()
    assertThat(s.isActive).isTrue()

    assertThat {
      s.getCompleted()
    }.isFailure()
      .hasMessage("This job has not completed yet")

    val awaitComplete = AtomicBoolean()

    launch(Dispatchers.Default) {
      assertThat(s.await()).isEqualTo("asdf")
      awaitComplete.set(true)
    }

    (s as CompletableDeferred).complete("asdf")
    assertThat(s.getCompleted()).isEqualTo("asdf")

    Awaitility.await().untilAtomicIsTrue(awaitComplete)
  }
}
