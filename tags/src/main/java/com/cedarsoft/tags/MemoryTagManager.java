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

package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A abstract implementation of {@link TagManager} that holds the tags in memory.
 * This implementation can be used when the instance of the tag itself is not important.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class MemoryTagManager<T> extends AbstractTagManager<T> {
  @NotNull
  protected final Map<T, WeakReference<Taggable>> store = new WeakHashMap<T, WeakReference<Taggable>>();

  /**
   * <p>Constructor for MemoryTagManager.</p>
   */
  protected MemoryTagManager() {
    super( new MemoryTagProvider() );
  }

  /**
   * {@inheritDoc}
   */
  @NotNull
  @Override
  protected TagSet createTaggable( @NotNull T o ) {
    TagSet taggable = new TagSet( o );
    store.put( o, new WeakReference<Taggable>( taggable ) );
    return taggable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Taggable findTaggable( @NotNull T o ) throws NotFoundException {
    WeakReference<Taggable> weakReference = store.get( o );
    if ( weakReference == null ) {
      throw new NotFoundException();
    }
    Taggable taggable = weakReference.get();
    if ( taggable == null ) {
      throw new NotFoundException();
    }
    return taggable;
  }

  /**
   * <p>getObject</p>
   *
   * @param taggable a {@link com.cedarsoft.tags.Taggable} object.
   * @return a T object.
   */
  @NotNull
  protected T getObject( @NotNull Taggable taggable ) {
    for ( Map.Entry<T, WeakReference<Taggable>> entry : store.entrySet() ) {
      //noinspection ObjectEquality
      if ( entry.getValue().get() == taggable ) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException( "No object found for " + taggable );
  }
}
