package it.neckar.open.javafx.properties

import assertk.*
import assertk.assertions.*
import it.neckar.open.javafx.consumeImmediately
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.System.gc
import java.util.concurrent.atomic.AtomicInteger

/**
 */
class WeakListenersTest {
  var foo: Foo = Foo()

  @BeforeEach
  internal fun setUp() {
    foo = Foo()
  }

  @Test
  internal fun testConsume() {
    assertThat(foo.name).isEqualTo("")

    val counter = AtomicInteger()

    assertThat(counter.get()).isEqualTo(0)
    registerListener(counter)

    assertThat(counter.get()).isEqualTo(1)
    foo.name = "asdf17"
    assertThat(counter.get()).isEqualTo(2)

    for (i in 1..10) {
      gc()
    }

    foo.name = "asdf2"
    assertThat(counter.get()).isEqualTo(3)
  }

  private fun registerListener(counter: AtomicInteger) {
    foo.nameProperty.consumeImmediately {
      counter.incrementAndGet()
    }
  }
}
