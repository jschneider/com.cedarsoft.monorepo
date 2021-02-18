package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class StringTest {
  @Test
  fun testContainsAll() {
    assertThat("a".containsAll(listOf("b"))).isFalse()
    assertThat("a".containsAll(listOf("a"))).isTrue()
    assertThat("a".containsAll(listOf("a", "b"))).isFalse()

    assertThat("foobar".containsAll(listOf("a", "b"))).isTrue()
    assertThat("foobar".containsAll(listOf("foo", "bar"))).isTrue()

    assertThat("foobar".containsAll(listOf("foo", "z", "bar"))).isFalse()
  }
}
