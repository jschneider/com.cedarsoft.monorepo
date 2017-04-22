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
 * Represents a city.
 */
public class City {
  @Nonnull
  public static final City NULL = new City( "", "" );
  @Nonnull

  public static final String PROPERTY_POSTAL_CODE = "postalCode";
  @Nonnull

  public static final String PROPERTY_NAME = "name";
  private Long id;
  @Nonnull

  private final String postalCode;
  @Nonnull

  private final String name;

  /**
   * Hibernate
   */
  @Deprecated
  protected City() {
    postalCode = "";
    name = "";
  }

  public City(  @Nonnull String postalCode,  @Nonnull String name ) {
    this.postalCode = postalCode;
    this.name = name;
  }

  @Nonnull

  public String getName() {
    return name;
  }

  @Nonnull

  public String getPostalCode() {
    return postalCode;
  }

  @Override
  public boolean equals( Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null || getClass() != obj.getClass() ) {
      return false;
    }

    City city = ( City ) obj;

    if ( name != null ? !name.equals( city.name ) : city.name != null ) {
      return false;
    }
    if ( postalCode != null ? !postalCode.equals( city.postalCode ) : city.postalCode != null ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = ( postalCode != null ? postalCode.hashCode() : 0 );
    result = 31 * result + ( name != null ? name.hashCode() : 0 );
    return result;
  }

  @Override
  public String toString() {
    return postalCode + ' ' + name;
  }
}