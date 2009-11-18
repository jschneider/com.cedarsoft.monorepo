package com.cedarsoft.license;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class CreativeCommonsLicense extends License {
  @NotNull
  public static final CreativeCommonsLicense CC_BY = new CreativeCommonsLicense( "CC-BY", "CC Attribution", false, ModificationsAllowed.YES );
  @NotNull
  public static final CreativeCommonsLicense CC_BY_SA = new CreativeCommonsLicense( "CC-BY-SA", "CC Attribution Share Alike", false, ModificationsAllowed.SHARE_ALIKE );
  @NotNull
  public static final CreativeCommonsLicense CC_BY_ND = new CreativeCommonsLicense( "CC-BY-ND", "CC Attribution No Derivates", false, ModificationsAllowed.NO );
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC = new CreativeCommonsLicense( "CC-BY-NC", "CC Attribution Non-Commercial", true, ModificationsAllowed.YES );
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC_SA = new CreativeCommonsLicense( "CC-BY-NC-SA", "CC Attribution Non-Commercial Share Alike", true, ModificationsAllowed.SHARE_ALIKE );
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC_ND = new CreativeCommonsLicense( "CC-BY-NC-ND", "CC Attribution Non-Commercial No Derivates", true, ModificationsAllowed.NO );

  @NotNull
  private final ModificationsAllowed modificationsAllowed;

  private final boolean restrictedToNonCommercial;


  public CreativeCommonsLicense( @NotNull @NonNls String id, @NotNull @NonNls String name, boolean restrictedToNonCommercial, @NotNull ModificationsAllowed modificationsAllowed ) {
    super( id, name );
    this.restrictedToNonCommercial = restrictedToNonCommercial;
    this.modificationsAllowed = modificationsAllowed;
  }

  public boolean isRestrictedToNonCommercial() {
    return restrictedToNonCommercial;
  }

  @NotNull
  public ModificationsAllowed getModificationsAllowed() {
    return modificationsAllowed;
  }

  public boolean isDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.YES;
  }

  public boolean isUsableCommercially() {
    return !isRestrictedToNonCommercial();
  }

  public boolean isSharedAlikeDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.SHARE_ALIKE || modificationsAllowed == ModificationsAllowed.YES;
  }

  public enum ModificationsAllowed {
    YES,
    SHARE_ALIKE,
    NO
  }
}
