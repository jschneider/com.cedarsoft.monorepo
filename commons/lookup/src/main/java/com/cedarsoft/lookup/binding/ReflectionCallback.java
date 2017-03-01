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

package com.cedarsoft.lookup.binding;

import com.cedarsoft.lookup.LookupChangeEvent;
import com.cedarsoft.lookup.LookupChangeListener;
import javax.annotation.Nonnull;

import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>ReflectionCallback class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ReflectionCallback<T> implements LookupChangeListener<T> {
  @Nonnull
  private final Object object;
  @Nonnull
  private final Method setter;

  /**
   * <p>Constructor for ReflectionCallback.</p>
   *
   * @param object a Object object.
   * @param setter a Method object.
   */
  public ReflectionCallback( @Nonnull Object object, @Nonnull Method setter ) {
    this.object = object;
    this.setter = setter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lookupChanged( @Nonnull LookupChangeEvent<? extends T> event ) {
    try {
      setter.invoke( object, event.getNewValue() );
    } catch ( IllegalAccessException e ) {
      throw new RuntimeException( e );
    } catch ( InvocationTargetException e ) {
      throw new RuntimeException( e );
    }
  }
}
