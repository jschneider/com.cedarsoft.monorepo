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

package com.cedarsoft;

import com.cedarsoft.properties.PropertiesPath;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that represents the change of something
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ChangedEvent<T> {
  @NotNull
  private final T changedObject;
  @NotNull
  private final PropertiesPath propertiesPath;
  @Nullable
  private final Object context;

  /**
   * <p>Constructor for ChangedEvent.</p>
   *
   * @param changedObject  a T object.
   * @param context        a {@link java.lang.Object} object.
   * @param propertiesPath a {@link java.lang.String} object.
   */
  public ChangedEvent( @NotNull T changedObject, @Nullable Object context, @NonNls @NotNull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }
    this.changedObject = changedObject;
    this.context = context;
    this.propertiesPath = new PropertiesPath( propertiesPath );
  }

  /**
   * <p>Getter for the field <code>propertiesPath</code>.</p>
   *
   * @return a {@link com.cedarsoft.properties.PropertiesPath} object.
   */
  @NotNull
  public PropertiesPath getPropertiesPath() {
    return propertiesPath;
  }

  /**
   * The context object
   *
   * @return the context object
   */
  @Nullable
  public Object getContext() {
    return context;
  }

  /**
   * The changed object
   *
   * @return the changed object
   */
  @NotNull
  public T getChangedObject() {
    return changedObject;
  }

  /**
   * <p>getRootProperty</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @Deprecated
  @NotNull
  @NonNls
  public String getRootProperty() {
    return propertiesPath.getRootProperty();
  }
}
