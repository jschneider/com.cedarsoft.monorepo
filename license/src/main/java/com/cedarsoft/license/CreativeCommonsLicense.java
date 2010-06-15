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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>CreativeCommonsLicense class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class CreativeCommonsLicense extends License {
  /** Constant <code>CC_BY</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY = new CreativeCommonsLicense( "CC-BY", "CC Attribution", false, ModificationsAllowed.YES );
  /** Constant <code>CC_BY_SA</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY_SA = new CreativeCommonsLicense( "CC-BY-SA", "CC Attribution Share Alike", false, ModificationsAllowed.SHARE_ALIKE );
  /** Constant <code>CC_BY_ND</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY_ND = new CreativeCommonsLicense( "CC-BY-ND", "CC Attribution No Derivates", false, ModificationsAllowed.NO );
  /** Constant <code>CC_BY_NC</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC = new CreativeCommonsLicense( "CC-BY-NC", "CC Attribution Non-Commercial", true, ModificationsAllowed.YES );
  /** Constant <code>CC_BY_NC_SA</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC_SA = new CreativeCommonsLicense( "CC-BY-NC-SA", "CC Attribution Non-Commercial Share Alike", true, ModificationsAllowed.SHARE_ALIKE );
  /** Constant <code>CC_BY_NC_ND</code> */
  @NotNull
  public static final CreativeCommonsLicense CC_BY_NC_ND = new CreativeCommonsLicense( "CC-BY-NC-ND", "CC Attribution Non-Commercial No Derivates", true, ModificationsAllowed.NO );

  @NotNull
  private final ModificationsAllowed modificationsAllowed;

  private final boolean restrictedToNonCommercial;


  /**
   * <p>Constructor for CreativeCommonsLicense.</p>
   *
   * @param id a {@link java.lang.String} object.
   * @param name a {@link java.lang.String} object.
   * @param restrictedToNonCommercial a boolean.
   * @param modificationsAllowed a {@link com.cedarsoft.license.CreativeCommonsLicense.ModificationsAllowed} object.
   */
  public CreativeCommonsLicense( @NotNull @NonNls String id, @NotNull @NonNls String name, boolean restrictedToNonCommercial, @NotNull ModificationsAllowed modificationsAllowed ) {
    super( id, name );
    this.restrictedToNonCommercial = restrictedToNonCommercial;
    this.modificationsAllowed = modificationsAllowed;
  }

  /**
   * <p>isRestrictedToNonCommercial</p>
   *
   * @return a boolean.
   */
  public boolean isRestrictedToNonCommercial() {
    return restrictedToNonCommercial;
  }

  /**
   * <p>Getter for the field <code>modificationsAllowed</code>.</p>
   *
   * @return a {@link com.cedarsoft.license.CreativeCommonsLicense.ModificationsAllowed} object.
   */
  @NotNull
  public ModificationsAllowed getModificationsAllowed() {
    return modificationsAllowed;
  }

  /**
   * <p>isDerivedWorkAllowed</p>
   *
   * @return a boolean.
   */
  public boolean isDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.YES;
  }

  /**
   * <p>isUsableCommercially</p>
   *
   * @return a boolean.
   */
  public boolean isUsableCommercially() {
    return !isRestrictedToNonCommercial();
  }

  /**
   * <p>isSharedAlikeDerivedWorkAllowed</p>
   *
   * @return a boolean.
   */
  public boolean isSharedAlikeDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.SHARE_ALIKE || modificationsAllowed == ModificationsAllowed.YES;
  }

  public enum ModificationsAllowed {
    YES,
    SHARE_ALIKE,
    NO
  }
}
