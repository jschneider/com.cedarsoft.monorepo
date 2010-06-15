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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * This implementation holds only *one* tag
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class SingleTaggable implements Taggable {
  private final TagChangeSupport tcs;

  @Nullable
  private Tag selectedTag;

  /**
   * <p>Constructor for SingleTaggable.</p>
   *
   * @param source a {@link java.lang.Object} object.
   */
  public SingleTaggable( @NotNull Object source ) {
    tcs = new TagChangeSupport( source );
  }

  /**
   * <p>Setter for the field <code>selectedTag</code>.</p>
   *
   * @param tag a {@link com.cedarsoft.tags.Tag} object.
   */
  public void setSelectedTag( @Nullable Tag tag ) {
    if ( tag == null ) {
      if ( selectedTag != null ) {
        removeTag( selectedTag );
      }
    } else {
      addTag( tag );
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean addTag( @NotNull Tag tag ) {
    //noinspection ObjectEquality
    if ( tag == selectedTag ) {
      return false;
    }

    if ( selectedTag != null ) {
      removeTag( selectedTag );
    }

    this.selectedTag = tag;
    tcs.notifyTagAdded( tag, 0 );
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeTag( @NotNull Tag tag ) {
    //noinspection ObjectEquality
    if ( selectedTag != tag ) {
      throw new IllegalArgumentException( "Tag not found: " + tag );
    }

    selectedTag = null;
    tcs.notifyTagRemoved( tag, 0 );
    return true;
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    if ( selectedTag != null ) {
      return Collections.singletonList( selectedTag );
    } else {
      return Collections.emptyList();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tcs.addTagChangeListener( listener );
  }

  /** {@inheritDoc} */
  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tcs.removeTagChangeListener( listener );
  }

  /**
   * <p>Getter for the field <code>selectedTag</code>.</p>
   *
   * @return a {@link com.cedarsoft.tags.Tag} object.
   */
  @Nullable
  public Tag getSelectedTag() {
    return selectedTag;
  }
}
