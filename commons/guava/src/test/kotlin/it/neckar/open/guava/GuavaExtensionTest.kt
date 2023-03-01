package it.neckar.open.guava

import com.google.common.collect.ImmutableList
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

/**
 */
class GuavaExtensionTest {
  @Test
  internal fun testAddCopy() {
    val original = ImmutableList.of("A", "B", "C")
    val extended = original.copyAndAdd("D")

    Assertions.assertThat(extended)
      .hasSize(4)
      .containsExactly("A", "B", "C", "D")
  }
}
