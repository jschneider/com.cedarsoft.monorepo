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

package com.cedarsoft.hierarchy;

import com.cedarsoft.cache.Cache;
import com.cedarsoft.cache.HashedCache;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Special implementation of a child detector that always returns the same list for a given parent.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <C> the type of the children
 * @param <P> the type of the parent
 */
public abstract class CachingChildDetector<P, C> extends AbstractChildDetector<P, C> {
  @NotNull
  private final Cache<P, List<? extends C>> childrenCache = new HashedCache<P, List<? extends C>>( new WeakHashMap<P, List<? extends C>>(), new Cache.Factory<P, List<? extends C>>() {
    @Override
    @NotNull
    public List<? extends C> create( @NotNull P key ) {
      return createChildren( key );
    }
  } );

  /**
   * <p>createChildren</p>
   *
   * @param parent a P object.
   * @return a {@link java.util.List} object.
   */
  @NotNull
  protected abstract List<? extends C> createChildren( @NotNull P parent );

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public final List<? extends C> findChildren( @NotNull P parent ) {
    return childrenCache.get( parent );
  }

  /**
   * <p>handleModified</p>
   *
   * @param parent a P object.
   */
  public void handleModified( @NotNull P parent ) {
    invalidateCache( parent );
  }

  /**
   * <p>invalidateCache</p>
   *
   * @param parent a P object.
   */
  public void invalidateCache( @NotNull P parent ) {
    childrenCache.remove( parent );
    notifyChildrenChangedFor( parent );
  }
}
