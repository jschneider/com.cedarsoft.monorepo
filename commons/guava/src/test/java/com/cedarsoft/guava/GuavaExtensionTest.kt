package com.cedarsoft.guava

import com.google.common.collect.ImmutableList
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
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