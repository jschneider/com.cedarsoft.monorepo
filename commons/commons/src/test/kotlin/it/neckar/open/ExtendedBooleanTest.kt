/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package it.neckar.open

import it.neckar.open.ExtendedBoolean
import it.neckar.open.ExtendedBoolean.Companion.valueOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale
import java.util.ResourceBundle

/**
 *
 */
class ExtendedBooleanTest {
  @BeforeEach
  fun setUp() {
    Locale.setDefault(Locale.US)
    ResourceBundle.clearCache()
  }

  @Test
  fun testResourceBundles() {
    Assertions.assertEquals("", ResourceBundle.getBundle(ExtendedBoolean::class.java.name).locale.language)
    Assertions.assertEquals("", ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.FRENCH).locale.language)
    Assertions.assertEquals("", ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.ENGLISH).locale.language)
    Assertions.assertEquals("", ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.US).locale.language)
    Assertions.assertEquals("de", ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.GERMANY).locale.language)
    Assertions.assertNotNull(ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.GERMANY))
    Assertions.assertNotNull(ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.US))
    Assertions.assertNotSame(
      ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.GERMANY),
      ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.ENGLISH)
    )
    Assertions.assertNotNull(ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.GERMANY))
    Assertions.assertNotNull(ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.US))
    Assertions.assertNotSame(
      ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.GERMANY),
      ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.ENGLISH)
    )
    Assertions.assertEquals("Yes", ResourceBundle.getBundle(ExtendedBoolean::class.java.name, Locale.US).getString("True"))
  }

  @Test
  fun testTranslation() {
    Assertions.assertEquals("Ja", ExtendedBoolean.True.getDescription(Locale.GERMANY))
    Assertions.assertEquals("Yes", ExtendedBoolean.True.getDescription(Locale.US))
    Assertions.assertEquals("Yes", ExtendedBoolean.True.getDescription(Locale.FRANCE))
    Assertions.assertEquals("Nein", ExtendedBoolean.False.getDescription(Locale.GERMANY))
    Assertions.assertEquals("No", ExtendedBoolean.False.getDescription(Locale.US))
    Assertions.assertEquals("No", ExtendedBoolean.False.getDescription(Locale.FRANCE))
    Assertions.assertEquals("?", ExtendedBoolean.Unknown.getDescription(Locale.GERMANY))
    Assertions.assertEquals("?", ExtendedBoolean.Unknown.getDescription(Locale.US))
    Assertions.assertEquals("?", ExtendedBoolean.Unknown.getDescription(Locale.FRANCE))
  }

  @Test
  fun testValueOf() {
    Assertions.assertEquals(ExtendedBoolean.True, valueOf(true))
    Assertions.assertEquals(ExtendedBoolean.False, valueOf(false))
  }

  @Test
  fun testTranslation2() {
    val old = Locale.getDefault()
    try {
      Locale.setDefault(Locale.GERMANY)
      Assertions.assertEquals("Ja", ExtendedBoolean.True.description)
      Assertions.assertEquals("Nein", ExtendedBoolean.False.description)
      Assertions.assertEquals("?", ExtendedBoolean.Unknown.description)
    } finally {
      Locale.setDefault(old)
    }
  }

  @Test
  fun testBasic() {
    Assertions.assertTrue(ExtendedBoolean.True.isTrue)
    Assertions.assertFalse(ExtendedBoolean.True.isFalse)
    Assertions.assertFalse(ExtendedBoolean.True.isUnknown)
    Assertions.assertFalse(ExtendedBoolean.False.isTrue)
    Assertions.assertTrue(ExtendedBoolean.False.isFalse)
    Assertions.assertFalse(ExtendedBoolean.False.isUnknown)
    Assertions.assertFalse(ExtendedBoolean.Unknown.isTrue)
    Assertions.assertFalse(ExtendedBoolean.Unknown.isFalse)
    Assertions.assertTrue(ExtendedBoolean.Unknown.isUnknown)
  }
}
