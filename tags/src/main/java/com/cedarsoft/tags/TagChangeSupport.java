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

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;

/**
 * Support class for TagChangeListeners
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TagChangeSupport {
  private final List<TagChangeListener> listeners = new ArrayList<TagChangeListener>();
  private Object source;

  /**
   * Attention! A source object must be set before this support can be used!
   * {@link #setSource(Object)}
   */
  public TagChangeSupport() {
  }

  /**
   * Creates a new TagChangeSupport for the given source
   *
   * @param source the source
   */
  public TagChangeSupport( @NotNull Object source ) {
    this.source = source;
  }

  /**
   * Sets the source. This source must be set if the default constructor has been used.
   *
   * @param source the source
   */
  public void setSource( @NotNull Object source ) {
    if ( this.source != null ) {
      throw new IllegalStateException( "The source has still been set!" );
    }
    this.source = source;
  }

  /**
   * <p>Getter for the field <code>source</code>.</p>
   *
   * @return a {@link Object} object.
   */
  @NotNull
  public Object getSource() {
    if ( source == null ) {
      throw new IllegalStateException( "No source has been set" );
    }
    return source;
  }

  /**
   * <p>addTagChangeListener</p>
   *
   * @param listener a {@link TagChangeListener} object.
   */
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    listeners.add( listener );
  }

  /**
   * <p>removeTagChangeListener</p>
   *
   * @param listener a {@link TagChangeListener} object.
   */
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    listeners.remove( listener );
  }

  /**
   * Notifies the listeners that a tag has been added
   *
   * @param tag   the tag
   * @param index the index
   */
  public void notifyTagAdded( @NotNull Tag tag, int index ) {
    notifyTagChanged( tag, TagChangeListener.TagEventType.ADD, index );
  }

  /**
   * Notifies the listeners that a tag has been removed
   *
   * @param tag   the tag
   * @param index the index
   */
  public void notifyTagRemoved( @NotNull Tag tag, int index ) {
    notifyTagChanged( tag, TagChangeListener.TagEventType.REMOVE, index );
  }

  /**
   * Notifies that listeners that a tag has been changed
   *
   * @param tag       the tag that has been chagned
   * @param eventType the event type
   * @param index     the index
   */
  public void notifyTagChanged( @NotNull Tag tag, @NotNull TagChangeListener.TagEventType eventType, int index ) {
    notifyTagChanged( new TagChangeListener.TagChangeEvent( getSource(), eventType, tag, index ) );
  }

  /**
   * notifies the listeners that a tag has been changed
   *
   * @param event the event
   */
  public void notifyTagChanged( @NotNull TagChangeListener.TagChangeEvent event ) {
    if ( !listeners.isEmpty() ) {
      for ( TagChangeListener listener : listeners ) {
        listener.tagChanged( event );
      }
    }
  }
}
