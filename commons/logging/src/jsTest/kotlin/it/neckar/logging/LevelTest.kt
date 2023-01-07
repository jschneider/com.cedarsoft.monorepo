package it.neckar.logging

import assertk.*
import assertk.assertions.*
import kotlin.test.Test

class LevelTest {
  @Test
  fun testCompare() {
    assertThat(Level.DEBUG.isEnabled(Level.DEBUG)).isTrue()
    assertThat(Level.DEBUG.isEnabled(Level.INFO)).isFalse()
    assertThat(Level.DEBUG.isEnabled(Level.WARN)).isFalse()
    assertThat(Level.DEBUG.isEnabled(Level.ERROR)).isFalse()

    assertThat(Level.INFO.isEnabled(Level.DEBUG)).isTrue()
    assertThat(Level.INFO.isEnabled(Level.INFO)).isTrue()
    assertThat(Level.INFO.isEnabled(Level.WARN)).isFalse()
    assertThat(Level.INFO.isEnabled(Level.ERROR)).isFalse()

    assertThat(Level.WARN.isEnabled(Level.DEBUG)).isTrue()
    assertThat(Level.WARN.isEnabled(Level.INFO)).isTrue()
    assertThat(Level.WARN.isEnabled(Level.WARN)).isTrue()
    assertThat(Level.WARN.isEnabled(Level.ERROR)).isFalse()

    assertThat(Level.ERROR.isEnabled(Level.DEBUG)).isTrue()
    assertThat(Level.ERROR.isEnabled(Level.INFO)).isTrue()
    assertThat(Level.ERROR.isEnabled(Level.WARN)).isTrue()
    assertThat(Level.ERROR.isEnabled(Level.ERROR)).isTrue()
  }
}
