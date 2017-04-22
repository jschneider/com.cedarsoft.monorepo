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

import com.cedarsoft.business.contact.communication.AbstractCommunicationChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public abstract class BaseAddress extends AbstractCommunicationChannel {
  @Nonnull
  public static final String PROPERTY_CITY = "city";

  @Nonnull
  public static final String PROPERTY_COUNTRY = "country";

  @Nonnull
  public static final String PROPERTY_ADDRESS_LINE = "addressLine";

  @Nonnull
  protected City city = new City( "", "" );
  @Nonnull
  protected Country country = Country.Germany;

  protected BaseAddress() {
  }

  protected BaseAddress( @Nonnull City city, @Nonnull Country country, @Nonnull Classification classification, boolean active ) {
    super( classification, active );
    this.city = city;
    this.country = country;
  }

  @Nonnull
  public City getCity() {
    lock.readLock().lock();
    try {
      return city;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setCity( @Nonnull City city ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_CITY, this.city, this.city = city );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nonnull
  public Country getCountry() {
    lock.readLock().lock();
    try {
      return country;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setCountry( @Nonnull Country country ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_COUNTRY, this.country, this.country = country );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  @Nonnull
  public String getRepresentation() {
    lock.readLock().lock();
    try {
      return getAddressLine() + ", " + getCity() + " (" + getCountry() + ')';
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Sets the address line
   *
   * @param addressLine the address line
   */
  public abstract void setAddressLine( @Nonnull String addressLine );

  @Nonnull
  public abstract String getAddressLine();

  public static boolean hasSameRepresentation( @Nullable BaseAddress first, @Nullable BaseAddress other ) {
    //noinspection ObjectEquality
    if ( first == other ) {
      return true;
    }

    if ( first != null ) {
      if ( other == null ) {
        return false;
      } else {
        return first.getRepresentation().equals( other.getRepresentation() );
      }
    } else {
      return false;
    }
  }
}
