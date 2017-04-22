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

package com.cedarsoft.business;


import javax.annotation.Nonnull;

/**
 *
 */
public class Bank {
  @Nonnull

  public static final String PROPERTY_CODE = "code";
  @Nonnull

  public static final String PROPERTY_IDENTIFICATION = "identification";

  @Nonnull
  public static final Bank NULL = new Bank( "", "" );

  private Long id;

  @Nonnull

  private final String code;
  @Nonnull

  private final String identification;

  /**
   * Hibernate
   */
  @Deprecated
  protected Bank() {
    code = null;
    identification = null;
  }

  public Bank( @Nonnull String code, @Nonnull String identification ) {
    this.code = code;
    this.identification = identification;
  }

  @Nonnull
  public String getCode() {
    return code;
  }

  @Nonnull
  public String getIdentification() {
    return identification;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Bank bank = ( Bank ) o;

    if ( !code.equals( bank.code ) ) return false;
    if ( !identification.equals( bank.identification ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = code.hashCode();
    result = 31 * result + identification.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Bank{" +
        "code='" + code + '\'' +
        ", name='" + identification + '\'' +
        '}';
  }
}
