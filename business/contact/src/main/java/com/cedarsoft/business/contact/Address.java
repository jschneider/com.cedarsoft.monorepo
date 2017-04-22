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

package com.cedarsoft.business.contact;


import javax.annotation.Nonnull;

/**
 * Represents an address.
 */
public class Address extends BaseAddress {
  private static final long serialVersionUID = -4770555205554903188L;


  @Nonnull
  public static final String PROPERTY_STREET = "street";

  @Nonnull

  private String street = "";

  public Address() {
  }

  @Override
  public void setAddressLine( @Nonnull String addressLine ) {
    setStreet( addressLine );
  }

  @Override
  @Nonnull

  public String getAddressLine() {
    return getStreet();
  }

  public Address( @Nonnull String street, @Nonnull City city, @Nonnull Country country ) {
    this( street, city, country, Classification.PRIVATE );
  }

  public Address( @Nonnull String street, @Nonnull City city, @Nonnull Country country, @Nonnull Classification classification ) {
    this( street, city, country, classification, false );
  }

  public Address( @Nonnull String street, @Nonnull City city, @Nonnull Country country, @Nonnull Classification classification, boolean active ) {
    super( city, country, classification, active );
    this.street = street;
  }

  @Nonnull
  public String getStreet() {
    lock.readLock().lock();
    try {
      return street;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setStreet( @Nonnull String street ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_STREET, this.street, this.street = street );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public String toString() {
    lock.readLock().lock();
    try {
      return street + ", " + city + " (" + country + ')';
    } finally {
      lock.readLock().unlock();
    }
  }
}
