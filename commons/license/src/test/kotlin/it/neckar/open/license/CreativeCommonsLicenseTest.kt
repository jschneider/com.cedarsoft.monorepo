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
package it.neckar.open.license

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Locale

/**
 *
 */
class CreativeCommonsLicenseTest {
  @Test
  fun testUrls() {
    assertNotNull(License.CC_BY.url)
    assertNotNull(License.CC_BY_NC.url)
    assertNotNull(License.CC_BY_NC_ND.url)
    assertNotNull(License.CC_BY_NC_SA.url)
    assertNotNull(License.CC_BY_ND.url)
    assertNotNull(License.CC_BY_SA.url)

    //Ensure different urls
    val urls: MutableSet<String> = HashSet()
    for (license in License.CC_LICENSES) {
      assertNotNull(license)
      val url = license.url
      if (url != null) {
        urls.add(url.toString())
      }
    }
    assertEquals(6, urls.size.toLong())
  }

  @Test
  fun testLicenseUrlTranslated() {
    assertNotNull(License.CC_BY.getUrl(Locale.GERMAN))
    assertEquals("http://creativecommons.org/licenses/by/3.0/de", License.CC_BY.getUrl(Locale.GERMAN).toString())
    assertEquals("http://creativecommons.org/licenses/by-nc-nd/3.0/de", License.CC_BY_NC_ND.getUrl(Locale.GERMAN).toString())
  }

  @Test
  fun testAll() {
    assertEquals(6, License.CC_LICENSES.size.toLong())
  }

  @Test
  fun testCCBy() {
    assertFalse(License.CC_BY.isRestrictedToNonCommercial)
    assertTrue(License.CC_BY.isDerivedWorkAllowed)
    assertTrue(License.CC_BY.isSharedAlikeDerivedWorkAllowed)
    assertTrue(License.CC_BY.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.YES, License.CC_BY.modificationsAllowed)
  }

  @Test
  fun testCCSA() {
    assertFalse(License.CC_BY_SA.isRestrictedToNonCommercial)
    assertFalse(License.CC_BY_SA.isDerivedWorkAllowed)
    assertTrue(License.CC_BY_SA.isSharedAlikeDerivedWorkAllowed)
    assertTrue(License.CC_BY_SA.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, License.CC_BY_SA.modificationsAllowed)
  }

  @Test
  fun testCCND() {
    assertFalse(License.CC_BY_ND.isRestrictedToNonCommercial)
    assertFalse(License.CC_BY_ND.isDerivedWorkAllowed)
    assertFalse(License.CC_BY_ND.isSharedAlikeDerivedWorkAllowed)
    assertTrue(License.CC_BY_ND.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.NO, License.CC_BY_ND.modificationsAllowed)
  }

  @Test
  fun testCCNC() {
    assertTrue(License.CC_BY_NC.isRestrictedToNonCommercial)
    assertTrue(License.CC_BY_NC.isDerivedWorkAllowed)
    assertTrue(License.CC_BY_NC.isSharedAlikeDerivedWorkAllowed)
    assertFalse(License.CC_BY_NC.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.YES, License.CC_BY_NC.modificationsAllowed)
  }

  @Test
  fun testCCNCSA() {
    assertTrue(License.CC_BY_NC_SA.isRestrictedToNonCommercial)
    assertFalse(License.CC_BY_NC_SA.isDerivedWorkAllowed)
    assertTrue(License.CC_BY_NC_SA.isSharedAlikeDerivedWorkAllowed)
    assertFalse(License.CC_BY_NC_SA.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, License.CC_BY_NC_SA.modificationsAllowed)
  }

  @Test
  fun testCCNCND() {
    assertTrue(License.CC_BY_NC_ND.isRestrictedToNonCommercial)
    assertFalse(License.CC_BY_NC_ND.isDerivedWorkAllowed)
    assertFalse(License.CC_BY_NC_ND.isSharedAlikeDerivedWorkAllowed)
    assertFalse(License.CC_BY_NC_ND.isUsableCommercially)
    assertEquals(CreativeCommonsLicense.ModificationsAllowed.NO, License.CC_BY_NC_ND.modificationsAllowed)
  }

  @Test
  fun testResolve() {
    assertEquals(20, License.LICENSES.size.toLong())
    assertEquals(6, License.CC_LICENSES.size.toLong())
    Assertions.assertSame(License.GPL_3, License[License.GPL_3.id])
    Assertions.assertSame(License.CC_BY_NC_ND, CreativeCommonsLicense[License.CC_BY_NC_ND.id])
  }
}
