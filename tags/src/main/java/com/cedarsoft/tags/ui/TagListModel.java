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

package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagChangeListener;
import com.cedarsoft.tags.TagObservable;
import com.cedarsoft.tags.TagProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Date: May 3, 2007<br>
 * Time: 2:21:23 PM<br>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TagListModel implements ListModel {
  protected final List<ListDataListener> listeners = new ArrayList<ListDataListener>();
  @NotNull
  protected final TagObservable availableTags;

  private boolean nullable;

  /**
   * <p>Constructor for TagListModel.</p>
   *
   * @param availableTags a {@link TagProvider} object.
   */
  public TagListModel( @NotNull TagProvider availableTags ) {
    this( availableTags, false );
  }

  /**
   * <p>Constructor for TagListModel.</p>
   *
   * @param availableTags a {@link TagObservable} object.
   * @param nullable      a boolean.
   */
  public TagListModel( @NotNull TagObservable availableTags, boolean nullable ) {
    this.availableTags = availableTags;
    this.nullable = nullable;
    availableTags.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        notifyTagChanged( event );
      }
    } );
  }

  /**
   * <p>isNullable</p>
   *
   * @return a boolean.
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize() {
    if ( nullable ) {
      return availableTags.getTags().size() + 1;
    } else {
      return availableTags.getTags().size();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Tag getElementAt( int index ) {
    if ( index < 0 ) {
      throw new IllegalArgumentException( "invalid index " + index );
    }

    if ( nullable ) {
      if ( index == 0 ) {
        return null;
      }
      return availableTags.getTags().get( index - 1 );
    } else {
      List<? extends Tag> tags = availableTags.getTags();
      if ( tags.size() <= index ) {
        return null;
      } else {
        return tags.get( index );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListDataListener( @NotNull ListDataListener l ) {
    listeners.add( l );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeListDataListener( @NotNull ListDataListener l ) {
    listeners.remove( l );
  }

  /**
   * <p>notifyTagChanged</p>
   *
   * @param event a {@link TagChangeListener.TagChangeEvent} object.
   */
  protected void notifyTagChanged( @NotNull TagChangeListener.TagChangeEvent event ) {
    if ( listeners.isEmpty() ) {
      return;
    }

    int type;
    int index0;
    int index1;
    switch ( event.getType() ) {
      case ADD:
        type = ListDataEvent.INTERVAL_ADDED;
        index0 = event.getIndex();
        index1 = event.getIndex();
        break;
      case REMOVE:
        type = ListDataEvent.INTERVAL_REMOVED;
        index0 = event.getIndex();
        index1 = event.getIndex();
        break;
      default:
        throw new IllegalStateException( "Uups" );
    }

    ListDataEvent listDataEvent = new ListDataEvent( this, type, index0, index1 );
    for ( ListDataListener listener : listeners ) {
      listener.contentsChanged( listDataEvent );
    }
  }

  /**
   * <p>Getter for the field <code>availableTags</code>.</p>
   *
   * @return a {@link TagObservable} object.
   */
  @NotNull
  public TagObservable getAvailableTags() {
    return availableTags;
  }
}
