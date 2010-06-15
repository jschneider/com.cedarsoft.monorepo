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
 * Represents the license of the image
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class License {
  /**
   * Constant <code>UNKNOWN</code>
   */
  @NotNull
  public static final License UNKNOWN = new License( "UNKNOWN", "Unknown" );
  /**
   * Constant <code>ALL_RIGHTS_RESERVED</code>
   */
  @NotNull
  public static final License ALL_RIGHTS_RESERVED = new License( "ALL_RIGHTS_RESERVED", "All rights reserved" );
  /**
   * Constant <code>PUBLIC_DOMAIN</code>
   */
  @NotNull
  public static final License PUBLIC_DOMAIN = new License( "PUBLIC_DOMAIN", "Public Domain" );

  @NotNull
  @NonNls
  private final String id;
  @NotNull
  @NonNls
  private final String name;

  /**
   * <p>Constructor for License.</p>
   *
   * @param id   a {@link java.lang.String} object.
   * @param name a {@link java.lang.String} object.
   */
  public License( @NotNull @NonNls String id, @NotNull @NonNls String name ) {
    this.id = id;
    this.name = name;
  }

  /**
   * <p>Getter for the field <code>name</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  public String getName() {
    return name;
  }

  /**
   * <p>Getter for the field <code>id</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof License ) ) return false;

    License license = ( License ) o;

    if ( !id.equals( license.id ) ) return false;
    if ( !name.equals( license.name ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }
}
