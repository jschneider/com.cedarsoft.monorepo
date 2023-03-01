package it.neckar.open.concurrent

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.untilAtomicIsTrue
import kotlinx.coroutines.*
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
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

    Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAtomicIsTrue(awaitComplete)
  }
}
