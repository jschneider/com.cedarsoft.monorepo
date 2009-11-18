package com.cedarsoft.license;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class CreativeCommonsLicenseTest {
  @Test
  public void testCCBy() {
    assertFalse( CreativeCommonsLicense.CC_BY.isRestrictedToNonCommercial() );
    assertTrue( CreativeCommonsLicense.CC_BY.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY.isSharedAlikeDerivedWorkAllowed() );
  }

  @Test
  public void testCCSA() {
    assertFalse( CreativeCommonsLicense.CC_BY_SA.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_SA.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_SA.isSharedAlikeDerivedWorkAllowed() );
  }

  @Test
  public void testCCND() {
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isDerivedWorkAllowed() );
    assertFalse( CreativeCommonsLicense.CC_BY_ND.isSharedAlikeDerivedWorkAllowed() );
  }

  @Test
  public void testCCNC() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isRestrictedToNonCommercial() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC.isSharedAlikeDerivedWorkAllowed() );
  }

  @Test
  public void testCCNCSA() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC_SA.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_SA.isDerivedWorkAllowed() );
    assertTrue( CreativeCommonsLicense.CC_BY_NC_SA.isSharedAlikeDerivedWorkAllowed() );
  }

  @Test
  public void testCCNCND() {
    assertTrue( CreativeCommonsLicense.CC_BY_NC_ND.isRestrictedToNonCommercial() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_ND.isDerivedWorkAllowed() );
    assertFalse( CreativeCommonsLicense.CC_BY_NC_ND.isSharedAlikeDerivedWorkAllowed() );
  }
}
