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

package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>FileType class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileType {

  @NotNull
  @NonNls
  private final List<Extension> extensions = new ArrayList<Extension>();

  @NotNull
  @NonNls
  private final String id;
  private final boolean dependentType;

  /**
   * <p>Constructor for FileType.</p>
   *
   * @param id            a {@link java.lang.String} object.
   * @param dependentType a boolean.
   * @param extensions    a {@link com.cedarsoft.file.Extension} object.
   */
  public FileType( @NotNull @NonNls String id, boolean dependentType, @NotNull @NonNls Extension... extensions ) {
    this( id, dependentType, Arrays.<Extension>asList( extensions ) );
  }

  /**
   * <p>Constructor for FileType.</p>
   *
   * @param id            a {@link java.lang.String} object.
   * @param dependentType a boolean.
   * @param extensions    a {@link java.util.Collection} object.
   */
  public FileType( @NotNull @NonNls String id, boolean dependentType, @NotNull @NonNls Collection<? extends Extension> extensions ) {
    this.dependentType = dependentType;
    assert !extensions.isEmpty();
    this.id = id;
    this.extensions.addAll( extensions );
  }

  /**
   * <p>Getter for the field <code>extensions</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends Extension> getExtensions() {
    return Collections.unmodifiableList( extensions );
  }

  /**
   * <p>matches</p>
   *
   * @param fileName a {@link java.lang.String} object.
   * @return a boolean.
   */
  public boolean matches( @NotNull @NonNls String fileName ) {
    for ( @NotNull @NonNls Extension ex : extensions ) {
      if ( fileName.toLowerCase().endsWith( ex.getCombined() ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>matches</p>
   *
   * @param fileName a {@link com.cedarsoft.file.FileName} object.
   * @return a boolean.
   */
  public boolean matches( @NotNull @NonNls FileName fileName ) {
    return matches( fileName.getName() );
  }

  /**
   * <p>getDefaultExtension</p>
   *
   * @return a {@link com.cedarsoft.file.Extension} object.
   */
  @NotNull
  @NonNls
  public Extension getDefaultExtension() {
    if ( extensions.isEmpty() ) {
      throw new IllegalStateException( "Cannot return a default extension for " + this );
    }
    return extensions.get( 0 );
  }

  /**
   * <p>Getter for the field <code>id</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  public String getId() {
    return id;
  }

  /**
   * Returns whether this file is a dependent type.
   * A dependent file is a file that needs another file it depends on.
   *
   * @return whether this file is a dependent type
   */
  public boolean isDependentType() {
    return dependentType;
  }

  /**
   * <p>getFileName</p>
   *
   * @param file a {@link java.io.File} object.
   * @return a {@link com.cedarsoft.file.FileName} object.
   */
  @NotNull
  public FileName getFileName( @NonNls @NotNull File file ) {
    return getFileName( file.getName() );
  }

  /**
   * <p>getFileName</p>
   *
   * @param fileName a {@link java.lang.String} object.
   * @return a {@link com.cedarsoft.file.FileName} object.
   */
  @NotNull
  public FileName getFileName( @NonNls @NotNull String fileName ) {
    @NonNls
    String bestBase = null;
    Extension bestExtension = null;

    for ( @NonNls Extension extension : extensions ) {
      int index = fileName.toLowerCase().indexOf( extension.getCombined() );
      if ( index < 0 ) {
        continue;
      }

      String base = fileName.substring( 0, index );
      if ( bestBase == null || base.length() < bestBase.length() ) {
        bestBase = base;
        bestExtension = extension;
      }
    }

    if ( bestBase == null ) {
      throw new IllegalArgumentException( "Cannot get base for " + fileName );
    }

    return new FileName( bestBase, bestExtension );
  }

  /**
   * <p>getExtension</p>
   *
   * @param fileName a {@link java.lang.String} object.
   * @return a {@link com.cedarsoft.file.Extension} object.
   */
  @NotNull
  @NonNls
  public Extension getExtension( @NonNls @NotNull String fileName ) {
    return getFileName( fileName ).getExtension();
  }

  /**
   * <p>getBaseName</p>
   *
   * @param fileName a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getBaseName( @NonNls @NotNull String fileName ) {
    return getFileName( fileName ).getBaseName().getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "FileType{" +
      "id='" + id + '\'' +
      '}';
  }

  /**
   * <p>isDefaultExtension</p>
   *
   * @param extension a {@link com.cedarsoft.file.Extension} object.
   * @return a boolean.
   */
  public boolean isDefaultExtension( @NotNull Extension extension ) {
    return getDefaultExtension().equals( extension );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileType ) ) return false;

    FileType fileType = ( FileType ) o;

    if ( dependentType != fileType.dependentType ) return false;
    if ( !extensions.equals( fileType.extensions ) ) return false;
    if ( !id.equals( fileType.id ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = extensions.hashCode();
    result = 31 * result + id.hashCode();
    result = 31 * result + ( dependentType ? 1 : 0 );
    return result;
  }
}
