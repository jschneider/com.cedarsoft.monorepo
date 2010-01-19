/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of a file
 */
public class Extension {
  @NonNls
  public static final String DEFAULT_DELIMITER = ".";
  @NotNull
  public static final Extension NONE = new Extension( "", "" );

  @NotNull
  @NonNls
  private final String delimiter;
  @NotNull
  @NonNls
  private final String extension;

  public Extension( @NotNull @NonNls String extension ) {
    this( DEFAULT_DELIMITER, extension );
  }

  public Extension( @NonNls @NotNull String delimiter, @NotNull @NonNls String extension ) {
    this.delimiter = delimiter;
    this.extension = extension;
  }

  @NotNull
  @NonNls
  public String getDelimiter() {
    return delimiter;
  }

  @NotNull
  @NonNls
  public String getExtension() {
    return extension;
  }

  @NotNull
  @NonNls
  public String getCombined() {
    return delimiter + extension;
  }

  @Override
  public String toString() {
    return getCombined();
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Extension ) ) return false;

    Extension extension1 = ( Extension ) o;

    if ( !delimiter.equals( extension1.delimiter ) ) return false;
    if ( !extension.equals( extension1.extension ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = delimiter.hashCode();
    result = 31 * result + extension.hashCode();
    return result;
  }
}
