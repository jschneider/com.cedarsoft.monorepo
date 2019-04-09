/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.version;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 *
 */
public class VersionTest {
  @Test
  public void testSmapper() throws Exception {
    assertThat(Version.valueOf(1, 0, 0).smallerThan(Version.valueOf(2, 0, 0))).isTrue();
    assertThat(Version.valueOf(3, 1, 0, "SNAPSHOT").smallerThan(Version.valueOf(2, 0, 0))).isFalse();
    assertThat(Version.valueOf(2, 0, 0).smallerThan(Version.valueOf(3, 1, 0, "SNAPSHOT"))).isTrue();
    assertThat(Version.valueOf(3, 0, 0).smallerThan(Version.valueOf(3, 1, 0, "SNAPSHOT"))).isTrue();
  }

  @Test
  public void parseLongNumber() throws Exception {
    Version version = Version.parse("0.0.110");
    assertThat(version.getMajor()).isEqualTo(0);
    assertThat(version.getMinor()).isEqualTo(0);
    assertThat(version.getBuild()).isEqualTo(110);
  }

  @Test
  public void testMatch() {
    Version.verifyMatch( Version.valueOf( 1, 2, 3 ), Version.valueOf( 1, 2, 3 ) );

    try {
      Version.verifyMatch(Version.valueOf(1, 2, 3), Version.valueOf(1, 2, 4));
      Assertions.fail("Where is the Exception");
    }
    catch (VersionMismatchException ignore) {
    }
  }

  @Test
  public void testToInt() {
    Assertions.assertEquals(10203, new Version(1, 2, 3).toInt());
    Assertions.assertEquals(123456, new Version(12, 34, 56).toInt());
  }

  @Test
  public void testEquals() {
    Assertions.assertEquals(new Version(1, 2, 3), new Version(1, 2, 3));
    Assertions.assertEquals(new Version(1, 2, 3, "asdf"), new Version(1, 2, 3, "asdf"));
  }

  @Test
  public void testToString() {
    Assertions.assertEquals("1.2.3", new Version(1, 2, 3).toString());
    Assertions.assertEquals("1.2.3-asdf", new Version(1, 2, 3, "asdf").toString());
  }

  @Test
  public void testParse() {
    Assertions.assertEquals(new Version(1, 2, 3), Version.parse("1.2.3"));
    Assertions.assertEquals(new Version(1, 2, 3, "build76"), Version.parse("1.2.3-build76"));
    Assertions.assertEquals(new Version(1, 2, 3, "build76"), Version.parse(new Version(1, 2, 3, "build76").toString()));
  }

  @Test
  public void testCompareGreater() {
    Assertions.assertTrue(new Version(1, 2, 3).sameOrGreaterThan(new Version(1, 2, 3)));
    Assertions.assertTrue(new Version(1, 2, 3).sameOrGreaterThan(new Version(1, 2, 2)));
    Assertions.assertTrue(new Version(1, 2, 3).sameOrGreaterThan(new Version(0, 2, 2)));

    Assertions.assertFalse(new Version(1, 2, 3).sameOrGreaterThan(new Version(1, 2, 4)));
  }

  @Test
  public void testCompareSmaller() {
    Assertions.assertTrue(new Version(1, 2, 3).sameOrSmallerThan(new Version(1, 2, 3)));
    Assertions.assertTrue(new Version(1, 2, 3).sameOrSmallerThan(new Version(1, 2, 4)));
    Assertions.assertTrue(new Version(1, 2, 3).sameOrSmallerThan(new Version(2, 2, 2)));

    Assertions.assertFalse(new Version(1, 2, 3).sameOrSmallerThan(new Version(0, 2, 4)));
  }

  @Test
  public void withMethods() throws Exception {
    Version version = Version.valueOf(1, 2, 3);
    assertThat(version.withSuffix("daSuffix").getSuffix()).isEqualTo("daSuffix");
  }
}
