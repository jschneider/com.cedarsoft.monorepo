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

package com.cedarsoft.business.contact.communication;

import com.cedarsoft.business.contact.Classification;

import javax.annotation.Nonnull;

/**
 *
 */
public abstract class NumberBasedCommunicationChannel extends AbstractCommunicationChannel {

  public static final String PROPERTY_AREA_CODE = "areaCode";

  public static final String PROPERTY_COUNTRY_CODE = "countryCode";

  public static final String PROPERTY_NUMBER = "number";

  @Nonnull

  protected String areaCode = "";
  @Nonnull

  protected String countryCode = "";
  @Nonnull

  protected String number = "";

  /**
   * Hibernate
   */
  @Deprecated
  protected NumberBasedCommunicationChannel() {
  }

  public NumberBasedCommunicationChannel(  @Nonnull String countryCode, @Nonnull  String areaCode, @Nonnull  String number ) {
    this.areaCode = areaCode;
    this.countryCode = countryCode;
    this.number = number;
  }

  protected NumberBasedCommunicationChannel(  @Nonnull String countryCode, @Nonnull  String areaCode, @Nonnull  String number, Classification classification ) {
    this.countryCode = countryCode;
    this.areaCode = areaCode;
    this.number = number;
    this.classification = classification;
  }

  @Nonnull

  public String getAreaCode() {
    lock.readLock().lock();
    try {
      return areaCode;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nonnull

  public String getCountryCode() {
    lock.readLock().lock();
    try {
      return countryCode;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nonnull

  public String getNumber() {
    lock.readLock().lock();
    try {
      return number;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setAreaCode(  @Nonnull String areaCode ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_AREA_CODE, this.areaCode, this.areaCode = areaCode );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setCountryCode(  @Nonnull String countryCode ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_COUNTRY_CODE, this.countryCode, this.countryCode = countryCode );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setNumber( @Nonnull  String number ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_NUMBER, this.number, this.number = number );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nonnull

  public String getRepresentation() {
    return getCountryCode() + ' ' + getAreaCode() + ' ' + getNumber();
  }
}
