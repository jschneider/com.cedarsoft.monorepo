/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.presenter.model;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.commons.struct.StructureChangedEvent;
import com.cedarsoft.commons.struct.StructureListener;
import com.cedarsoft.lookup.LookupChangeEvent;
import com.cedarsoft.lookup.LookupChangeListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 */
public class StructBasedListModel implements ListModel {
  @NotNull
  protected final StructPart node;
  private final List<ListDataListener> listeners = new ArrayList<ListDataListener>();

  private final Map<StructPart, ChildLookupChangeListener> childListeners = new WeakHashMap<StructPart, ChildLookupChangeListener>();

  @SuppressWarnings( {"FieldCanBeLocal"} )
  //Keep as field --> is only added weak
  private final StructureListener structureListener;

  public StructBasedListModel( @NotNull StructPart node ) {
    this.node = node;

    //Register the lookup change listeners to all children
    for ( StructPart child : node.getChildren() ) {
      addChildLookupListener( child );
    }

    structureListener = new StructureListener() {
      @Override
      public void childAdded( @NotNull StructureChangedEvent event ) {
        addChildLookupListener( event.getStructPart() );

        //Notify the listeners that an entry has been added
        int index = event.getIndex();
        for ( ListDataListener listener : listeners ) {
          listener.intervalAdded( new ListDataEvent( StructBasedListModel.this, ListDataEvent.INTERVAL_ADDED, index, index ) );
        }
      }

      @Override
      public void childDetached( @NotNull StructureChangedEvent event ) {
        detachChildLookupListener( event.getStructPart() );

        //Notify the listeners that an entry has been removed
        int index = event.getIndex();
        for ( ListDataListener listener : listeners ) {
          listener.intervalRemoved( new ListDataEvent( StructBasedListModel.this, ListDataEvent.INTERVAL_REMOVED, index, index ) );
        }
      }
    };

    node.addStructureListenerWeak( structureListener );
  }

  /**
   * Detaches the child lookup listener
   *
   * @param child the child the listener is detached from
   */
  protected void detachChildLookupListener( @NotNull StructPart child ) {
    StructBasedListModel.ChildLookupChangeListener listener = childListeners.remove( child );
    if ( listener == null ) {
      throw new IllegalStateException( "No listener found for " + child );
    }
    child.getLookup().removeChangeListener( listener );
  }

  /**
   * Registers a lookup listener at the given child
   *
   * @param child the child the lookup listener is registered at
   */
  protected final void addChildLookupListener( @NotNull StructPart child ) {
    ChildLookupChangeListener listener = new ChildLookupChangeListener( child );
    if ( childListeners.get( child ) != null ) {
      throw new IllegalStateException( "Still added a listener for the given child " + child );
    }
    childListeners.put( child, listener );
    child.getLookup().addChangeListenerWeak( listener );
  }

  @Override
  public int getSize() {
    return node.getChildren().size();
  }

  @Override
  @NotNull
  public StructPart getElementAt( int index ) {
    return node.getChildren().get( index );
  }

  @Override
  public void addListDataListener( ListDataListener l ) {
    listeners.add( l );
  }

  @Override
  public void removeListDataListener( ListDataListener l ) {
    listeners.remove( l );
  }

  /**
   * A listener that notifies all listeners that a lookup has been changed
   */
  private class ChildLookupChangeListener implements LookupChangeListener<Object> {
    @NotNull
    private final StructPart part;

    private ChildLookupChangeListener( @NotNull StructPart part ) {
      this.part = part;
    }

    @Override
    public void lookupChanged( @NotNull LookupChangeEvent<?> event ) {
      int index = node.getChildren().indexOf( part );

      for ( ListDataListener listener : listeners ) {
        listener.contentsChanged( new ListDataEvent( StructBasedListModel.this, ListDataEvent.CONTENTS_CHANGED, index, index ) );
      }
    }
  }
}

