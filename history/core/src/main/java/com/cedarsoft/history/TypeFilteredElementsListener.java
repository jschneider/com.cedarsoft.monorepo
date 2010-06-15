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

package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 * This listener wraps another listener that only wants to listen for a subclass of the given type.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <D> the type of the ObservableCollection
 * @param <T> the type of the delegating listener
 */
@Deprecated
public class TypeFilteredElementsListener<D, T extends D> implements ElementListener<D> {
  @NotNull
  private final ElementListener<T> delegate;
  @NotNull
  private final Class<T> type;

  /**
   * <p>Constructor for TypeFilteredElementsListener.</p>
   *
   * @param type     a {@link java.lang.Class} object.
   * @param delegate a {@link com.cedarsoft.history.ElementListener} object.
   */
  public TypeFilteredElementsListener( @NotNull Class<T> type, @NotNull ElementListener<T> delegate ) {
    this.delegate = delegate;
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void elementDeleted( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementDeleted( type.cast( element ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void elementAdded( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementAdded( type.cast( element ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void elementChanged( @NotNull D element ) {
    if ( isValidType( element ) ) {
      delegate.elementChanged( type.cast( element ) );
    }
  }

  private boolean isValidType( @NotNull D element ) {
    return type.isAssignableFrom( element.getClass() );
  }
}
