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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class for {@link TagManager}
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class AbstractTagManager<T> implements TagManager<T> {
  @NotNull
  private final TagProvider tagProvider;

  /**
   * Creates a new abstract tag manager with the given factory
   *
   * @param tagProvider a {@link TagProvider} object.
   */
  protected AbstractTagManager( @NotNull TagProvider tagProvider ) {
    this.tagProvider = tagProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Tag getTag( @NotNull @NonNls String description ) {
    return tagProvider.getTag( description );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tagProvider.getTags();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Tag findTag( @NonNls @NotNull String description ) throws NotFoundException {
    return tagProvider.findTag( description );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTag( @NotNull Tag tag ) {
    tagProvider.removeTag( tag );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagProvider.addTagChangeListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagProvider.removeTagChangeListener( listener );
  }

  /**
   * Creates the taggable for the given object
   *
   * @param o th eobject
   * @return the taggable
   */
  @NotNull
  protected abstract Taggable createTaggable( @NotNull T o );

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Taggable getTaggable( @NotNull T o ) {
    try {
      return findTaggable( o );
    } catch ( NotFoundException ignore ) {
      return createTaggable( o );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getTagsAsString( @NotNull T o ) {
    List<? extends Tag> tags = getTaggable( o ).getTags();
    StringBuilder s = new StringBuilder();
    for ( Iterator<? extends Tag> it = tags.iterator(); it.hasNext(); ) {
      Tag tag = it.next();
      s.append( tag.getDescription() );
      if ( it.hasNext() ) {
        s.append( ", " );
      }
    }

    return s.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTag( @NotNull T object, @NotNull String description ) {
    getTaggable( object ).addTag( getTag( description ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTag( @NotNull T object, @NotNull String description ) {
    getTaggable( object ).removeTag( getTag( description ) );
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Remove the tag.
   * Handle with care!
   */
  @Override
  public void removeTag( @NonNls @NotNull String description ) throws NotFoundException {
    tagProvider.removeTag( description );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Tagged findTagged( @NotNull T o ) throws NotFoundException {
    return findTaggable( o );
  }
}
