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

import java.util.List;

/**
 * Holds the tags within a tag set
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class MemoryTagProvider extends AbstractTagProvider {
  @NotNull
  protected final TagSet tags = new TagSet( this );

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Tag createTag( @NotNull @NonNls String description ) {
    Tag newTag = new Tag( description );
    tags.addTag( newTag );
    return newTag;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tags.addTagChangeListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tags.removeTagChangeListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tags.getTags();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTag( @NotNull Tag tag ) {
    tags.removeTag( tag );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Tag findTag( @NonNls @NotNull String description ) throws NotFoundException {
    for ( Tag tag : tags.getTags() ) {
      if ( tag.getDescription().equals( description ) ) {
        return tag;
      }
    }
    throw new NotFoundException( "No tag found for description <" + description + '>' );
  }
}
