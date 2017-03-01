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

package com.cedarsoft.lookup;

import javax.annotation.Nonnull;

import java.lang.Class;

/**
 * <p>InstantiatorLookup class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class InstantiatorLookup<T> extends LazyLookup<T> {
  @Nonnull
  private final Instantiater<T> instantiater;

  private Class<? extends T> type;

  /**
   * <p>Constructor for InstantiatorLookup.</p>
   *
   * @param type         a Class object.
   * @param instantiater a Instantiater object.
   */
  public InstantiatorLookup( @Nonnull Class<? extends T> type, @Nonnull Instantiater<T> instantiater ) {
    this.type = type;
    this.instantiater = instantiater;
  }

  /**
   * <p>Constructor for InstantiatorLookup.</p>
   *
   * @param instantiater a Instantiater.Typed object.
   */
  public InstantiatorLookup( @Nonnull Instantiater.Typed<T> instantiater ) {
    this.instantiater = instantiater;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected T createInstance() {
    try {
      return instantiater.createInstance();
    } catch ( InstantiationFailedException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends T> getType() {
    if ( type != null ) {
      return type;
    }
    return ( ( Instantiater.Typed<T> ) instantiater ).getType();
  }
}
