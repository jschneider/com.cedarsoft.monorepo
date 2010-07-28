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

package com.cedarsoft.license;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class CreativeCommonsLicenseTest {
  @Test
  public void testCCBy() {
    assertFalse( CreativeCommonsLicense.CC_BY.isRestrictedToNonCommercial() );
    assertTrue( CreativeCommonsLicense.CC_BY.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY.isSharedAlikeDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.YES, CreativeCommonsLicense.CC_BY.getModificationsAllowed() );
  }

  @Test
  public void testCCSA() {
    assertFalse( CreativeCommonsLicense.CC_BY_SA.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_SA.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_SA.isSharedAlikeDerivedWorkAllowed() );

    assertTrue( CreativeCommonsLicense.CC_BY_SA.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, CreativeCommonsLicense.CC_BY_SA.getModificationsAllowed() );
  }

  @Test
  public void testCCND() {
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isDerivedWorkAllowed() );
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isSharedAlikeDerivedWorkAllowed() );

    assertTrue( CreativeCommonsLicense.CC_BY_ND.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.NO, CreativeCommonsLicense.CC_BY_ND.getModificationsAllowed() );
  }

  @Test
  public void testCCNC() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isRestrictedToNonCommercial() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isSharedAlikeDerivedWorkAllowed() );

    assertFalse( CreativeCommonsLicense.CC_BY_NC.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.YES, CreativeCommonsLicense.CC_BY_NC.getModificationsAllowed() );
  }

  @Test
  public void testCCNCSA() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC_SA.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_SA.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC_SA.isSharedAlikeDerivedWorkAllowed() );

    assertFalse( CreativeCommonsLicense.CC_BY_NC_SA.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, CreativeCommonsLicense.CC_BY_NC_SA.getModificationsAllowed() );
  }

  @Test
  public void testCCNCND() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC_ND.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_ND.isDerivedWorkAllowed() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_ND.isSharedAlikeDerivedWorkAllowed() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_ND.isUsableCommercially() );

    assertEquals( CreativeCommonsLicense.ModificationsAllowed.NO, CreativeCommonsLicense.CC_BY_NC_ND.getModificationsAllowed() );
  }
}
