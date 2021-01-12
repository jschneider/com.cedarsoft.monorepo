package com.cedarsoft.version

import com.cedarsoft.version.Version.Companion.parse
import com.cedarsoft.version.Version.Companion.valueOf
import com.cedarsoft.version.Version.Companion.verifyMatch
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

/**
 *
 */
class VersionTest {
  @Test
  @Throws(Exception::class)
  fun testSmapper() {
    Assertions.assertThat(valueOf(1, 0, 0).smallerThan(valueOf(2, 0, 0))).isTrue
    Assertions.assertThat(valueOf(3, 1, 0, "SNAPSHOT").smallerThan(valueOf(2, 0, 0))).isFalse
    Assertions.assertThat(valueOf(2, 0, 0).smallerThan(valueOf(3, 1, 0, "SNAPSHOT"))).isTrue
    Assertions.assertThat(valueOf(3, 0, 0).smallerThan(valueOf(3, 1, 0, "SNAPSHOT"))).isTrue
  }

  @Test
  @Throws(Exception::class)
  fun parseLongNumber() {
    val version = parse("0.0.110")
    Assertions.assertThat(version.major).isEqualTo(0)
    Assertions.assertThat(version.minor).isEqualTo(0)
    Assertions.assertThat(version.build).isEqualTo(110)
  }

  @Test
  fun testMatch() {
    verifyMatch(valueOf(1, 2, 3), valueOf(1, 2, 3))
    try {
      verifyMatch(valueOf(1, 2, 3), valueOf(1, 2, 4))
      fail<Any>("Where is the Exception")
    } catch (ignore: VersionMismatchException) {
    }
  }

  @Test
  fun testToInt() {
    assertEquals(10203, Version(1, 2, 3).toInt())
    assertEquals(123456, Version(12, 34, 56).toInt())
  }

  @Test
  fun testEquals() {
    assertEquals(Version(1, 2, 3), Version(1, 2, 3))
    assertEquals(Version(1, 2, 3, "asdf"), Version(1, 2, 3, "asdf"))
  }

  @Test
  fun testToString() {
    assertEquals("1.2.3", Version(1, 2, 3).toString())
    assertEquals("1.2.3-asdf", Version(1, 2, 3, "asdf").toString())
  }

  @Test
  fun testParse() {
    assertEquals(Version(1, 2, 3), parse("1.2.3"))
    assertEquals(Version(1, 2, 3, "build76"), parse("1.2.3-build76"))
    assertEquals(Version(1, 2, 3, "build76"), parse(Version(1, 2, 3, "build76").toString()))
  }

  @Test
  fun testCompareGreater() {
    assertTrue(Version(1, 2, 3).sameOrGreaterThan(Version(1, 2, 3)))
    assertTrue(Version(1, 2, 3).sameOrGreaterThan(Version(1, 2, 2)))
    assertTrue(Version(1, 2, 3).sameOrGreaterThan(Version(0, 2, 2)))
    assertFalse(Version(1, 2, 3).sameOrGreaterThan(Version(1, 2, 4)))
  }

  @Test
  fun testCompareSmaller() {
    assertTrue(Version(1, 2, 3).sameOrSmallerThan(Version(1, 2, 3)))
    assertTrue(Version(1, 2, 3).sameOrSmallerThan(Version(1, 2, 4)))
    assertTrue(Version(1, 2, 3).sameOrSmallerThan(Version(2, 2, 2)))
    assertFalse(Version(1, 2, 3).sameOrSmallerThan(Version(0, 2, 4)))
  }

  @Test
  @Throws(Exception::class)
  fun withMethods() {
    val version = valueOf(1, 2, 3)
    Assertions.assertThat(version.withSuffix("daSuffix").suffix).isEqualTo("daSuffix")
  }

}
