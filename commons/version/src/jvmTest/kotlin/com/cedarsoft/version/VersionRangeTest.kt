package com.cedarsoft.version

import com.cedarsoft.version.Version.Companion.valueOf
import com.cedarsoft.version.VersionRange.Companion.from
import com.cedarsoft.version.VersionRange.Companion.fromVersions
import com.cedarsoft.version.VersionRange.Companion.single
import com.google.common.collect.ImmutableList
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.lang.IllegalArgumentException

/**
 *
 */
class VersionRangeTest {
  @Test
  fun testSame() {
    val (min, max) = from(1, 1, 1).to(1, 1, 1)
    Assertions.assertThat(min).isEqualTo(valueOf(1, 1, 1))
    Assertions.assertThat(max).isEqualTo(valueOf(1, 1, 1))
  }

  @Test
  @Throws(Exception::class)
  fun fromList() {
    Assertions.assertThat(fromVersions(ImmutableList.of(valueOf(1, 0, 0), valueOf(2, 0, 0)))).isEqualTo(from(1, 0, 0).to(2, 0, 0))
    try {
      fromVersions(ImmutableList.of())
      fail<Any>("Where is the Exception")
    } catch (e: IllegalArgumentException) {
    }
    Assertions.assertThat(fromVersions(ImmutableList.of(valueOf(1, 0, 0)))).isEqualTo(from(1, 0, 0).to(1, 0, 0))
    Assertions.assertThat(fromVersions(ImmutableList.of(valueOf(2, 0, 0), valueOf(1, 0, 0)))).isEqualTo(from(1, 0, 0).to(2, 0, 0))
    Assertions.assertThat(fromVersions(ImmutableList.of(valueOf(1, 5, 0), valueOf(2, 0, 0), valueOf(1, 0, 0)))).isEqualTo(from(1, 0, 0).to(2, 0, 0))
  }

  @Test
  fun testConstructor() {
    from(1, 0, 0).to(2, 0, 0)
    from(1, 0, 0).to(1, 0, 0)
    try {
      from(1, 0, 1).to(1, 0, 0)
      fail<Any>("Where is the Exception")
    } catch (ignore: Exception) {
    }
  }

  @Test
  fun testFormat() {
    MatcherAssert.assertThat(single(1, 0, 0).format(), CoreMatchers.`is`("[1.0.0]"))
    MatcherAssert.assertThat(from(1, 0, 0).to(1, 0, 0).format(), CoreMatchers.`is`("[1.0.0]"))
    MatcherAssert.assertThat(from(1, 0, 0).to(2, 0, 0).format(), CoreMatchers.`is`("[1.0.0-2.0.0]"))
  }

  @Test
  fun testSingle() {
    MatcherAssert.assertThat(single(1, 0, 0).max, CoreMatchers.`is`(valueOf(1, 0, 0)))
    MatcherAssert.assertThat(single(1, 1, 0).max, CoreMatchers.`is`(valueOf(1, 1, 0)))
    MatcherAssert.assertThat(single(valueOf(1, 0, 0)).max, CoreMatchers.`is`(valueOf(1, 0, 0)))
    MatcherAssert.assertThat(single(valueOf(1, 1, 0)).max, CoreMatchers.`is`(valueOf(1, 1, 0)))
  }

  @Test
  fun testFluent() {
    assertEquals(from(1, 0, 0).to(2, 0, 0), VersionRange(Version(1, 0, 0), Version(2, 0, 0)))
  }

  @Test
  fun testMinMax() {
    run {
      val (min, max) = VersionRange(Version(1, 0, 0), Version(2, 0, 0))
      assertEquals(min, Version(1, 0, 0))
      assertEquals(max, Version(2, 0, 0))
    }
    run {
      val (min, max) = VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, false)
      assertEquals(min, Version(1, 0, 0))
      assertEquals(max, Version(2, 0, 0))
    }
  }

  @Test
  fun testOverlap() {
    val range = VersionRange(Version(1, 0, 0), Version(2, 0, 0))
    assertTrue(range.overlaps(VersionRange(Version(1, 0, 0), Version(2, 0, 0))))
    assertTrue(range.overlaps(VersionRange(Version(1, 0, 0), Version(1, 0, 0))))
    assertTrue(range.overlaps(VersionRange(Version(2, 0, 0), Version(2, 0, 0))))
    assertFalse(range.overlaps(VersionRange(Version(0, 0, 0), Version(0, 99, 99))))
    assertFalse(range.overlaps(VersionRange(Version(0, 0, 0), Version(1, 0, 0), true, false)))
    assertTrue(range.overlaps(VersionRange(Version(0, 0, 0), Version(1, 0, 0), true, true)))
    assertFalse(range.overlaps(VersionRange(Version(2, 0, 1), Version(3, 0, 0))))
    assertFalse(range.overlaps(VersionRange(Version(2, 0, 0), Version(3, 0, 0), false, true)))
    assertTrue(range.overlaps(VersionRange(Version(2, 0, 0), Version(3, 0, 0), true, true)))
  }

  @Test
  fun testContains() {
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0)).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0))))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0)).containsCompletely(VersionRange(Version(1, 0, 0), Version(1, 0, 0))))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0)).containsCompletely(VersionRange(Version(2, 0, 0), Version(2, 0, 0))))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0))))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0))))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, false)))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false)))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true)))
    assertTrue(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true)))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true)))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false)))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true)))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true)))
    assertFalse(VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, false).containsCompletely(VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true)))
  }

  @Test
  fun testExclude() {
    run {
      val range = VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, true)
      assertTrue(range.contains(Version(1, 0, 0)))
      assertTrue(range.contains(Version(1, 0, 1)))
      assertTrue(range.contains(Version(1, 99, 99)))
      assertTrue(range.contains(Version(2, 0, 0)))
      assertEquals("[1.0.0-2.0.0]", range.toString())
    }
    run {
      val range = VersionRange(Version(1, 0, 0), Version(2, 0, 0), true, false)
      assertTrue(range.contains(Version(1, 0, 0)))
      assertTrue(range.contains(Version(1, 0, 1)))
      assertTrue(range.contains(Version(1, 99, 99)))
      assertFalse(range.contains(Version(2, 0, 0)))
      assertEquals("[1.0.0-2.0.0[", range.toString())
    }
    run {
      val range = VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, true)
      assertFalse(range.contains(Version(1, 0, 0)))
      assertTrue(range.contains(Version(1, 0, 1)))
      assertTrue(range.contains(Version(1, 99, 99)))
      assertTrue(range.contains(Version(2, 0, 0)))
      assertEquals("]1.0.0-2.0.0]", range.toString())
    }
    run {
      val range = VersionRange(Version(1, 0, 0), Version(2, 0, 0), false, false)
      assertFalse(range.contains(Version(1, 0, 0)))
      assertTrue(range.contains(Version(1, 0, 1)))
      assertTrue(range.contains(Version(1, 99, 99)))
      assertFalse(range.contains(Version(2, 0, 0)))
      assertEquals("]1.0.0-2.0.0[", range.toString())
    }
  }

  @Test
  fun testIt() {
    val range = VersionRange(Version(1, 0, 0), Version(1, 1, 90))
    assertTrue(range.contains(Version(1, 0, 0)))
    assertTrue(range.contains(Version(1, 1, 0)))
    assertTrue(range.contains(Version(1, 1, 89)))
    assertTrue(range.contains(Version(1, 1, 90)))
    assertFalse(range.contains(Version(1, 1, 91)))
    assertFalse(range.contains(Version(1, 2, 0)))
    assertFalse(range.contains(Version(0, 99, 99)))
  }

  @Test
  fun testEquals() {
    assertEquals(VersionRange(Version(1, 0, 0), Version(1, 1, 90)), VersionRange(Version(1, 0, 0), Version(1, 1, 90)))
    assertEquals(VersionRange(Version(1, 0, 0), Version(2, 1, 90)), VersionRange(Version(1, 0, 0), Version(2, 1, 90)))
    assertNotEquals(VersionRange(Version(1, 0, 0), Version(2, 1, 90)), VersionRange(Version(1, 0, 0), Version(2, 1, 91)))
    assertNotEquals(VersionRange(Version(1, 0, 0), Version(2, 1, 90)), VersionRange(Version(1, 0, 0), Version(1, 1, 90)))
    assertNotEquals(VersionRange(Version(1, 0, 1), Version(2, 1, 90)), VersionRange(Version(1, 0, 0), Version(2, 1, 90)))
  }

  @Test
  fun testToString() {
    assertEquals("[1.0.0-1.1.90]", VersionRange(Version(1, 0, 0), Version(1, 1, 90)).toString())
  }

}
