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
package com.cedarsoft.swing.common.tree;

import javax.annotation.Nonnull;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

/**
 */
public abstract class AbstractTreeModel implements TreeModel {
  @Nonnull
  private final EventListenerList listenerList = new EventListenerList();

  /**
   * Adds a listener for the TreeModelEvent posted after the tree changes.
   *
   * @param l the listener to add
   * @see #removeTreeModelListener
   */
  @Override
  public void addTreeModelListener( TreeModelListener l ) {
    listenerList.add( TreeModelListener.class, l );
  }

  /**
   * Removes a listener previously added with <B>addTreeModelListener() </B>.
   *
   * @param l the listener to remove
   * @see #addTreeModelListener
   */
  @Override
  public void removeTreeModelListener( TreeModelListener l ) {
    listenerList.remove( TreeModelListener.class, l );
  }

  /**
   * Returns an array of all the tree model listeners registered on this
   * model.
   *
   * @return all of this model's <code>TreeModelListener</code> s or an
   *         empty array if no tree model listeners are currently registered
   *
   * @see #addTreeModelListener
   * @see #removeTreeModelListener
   */
  public TreeModelListener[] getTreeModelListeners() {
    return listenerList.getListeners(TreeModelListener.class );
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the
   * parameters passed into the fire method.
   *
   * @param path         the path to the root node
   * @param childIndices the indices of the changed elements
   * @param children     the changed elements
   * @see javax.swing.event.EventListenerList
   */
  protected void fireTreeNodesChanged( Object[] path, int[] childIndices, Object[] children ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        ( ( TreeModelListener ) listeners[i + 1] ).treeNodesChanged( new TreeModelEvent( this, path, childIndices, children ) );
      }
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the
   * parameters passed into the fire method.
   *
   * @param path         the path to the root node
   * @param childIndices the indices of the new elements
   * @param children     the new elements
   * @see javax.swing.event.EventListenerList
   */
  protected void fireTreeNodesInserted( Object[] path, int[] childIndices, Object[] children ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        if ( e == null ) {
          e = new TreeModelEvent( this, path, childIndices, children );
        }
        ( ( TreeModelListener ) listeners[i + 1] ).treeNodesInserted( e );
      }
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the
   * parameters passed into the fire method.
   *
   * @param path         the path to the root node
   * @param childIndices the indices of the removed elements
   * @param children     the removed elements
   * @see javax.swing.event.EventListenerList
   */
  protected void fireTreeNodesRemoved( Object[] path, int[] childIndices, Object[] children ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        ( ( TreeModelListener ) listeners[i + 1] ).treeNodesRemoved( new TreeModelEvent( this, path, childIndices, children ) );
      }
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the
   * parameters passed into the fire method.
   *
   * @param path         the path to the root node
   * @param childIndices the indices of the affected elements
   * @param children     the affected elements
   * @see javax.swing.event.EventListenerList
   */
  protected void fireTreeStructureChanged( Object[] path, int[] childIndices, Object[] children ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        if ( e == null ) {
          e = new TreeModelEvent( this, path, childIndices, children );
        }
        ( ( TreeModelListener ) listeners[i + 1] ).treeStructureChanged( e );
      }
    }
  }

  /**
   * The only event raised by this model is TreeStructureChanged with the root
   * as path, i.e. the whole tree has changed.
   *
   * @param previousRoot the previous root
   */
  protected void fireRootNodeChanged( @Nonnull Object previousRoot ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        if ( e == null ) {
          e = new TreeModelEvent( this, new Object[]{previousRoot} );
        }
        ( ( TreeModelListener ) listeners[i + 1] ).treeNodesChanged( e );
      }
    }
  }

  /**
   * The only event raised by this model is TreeStructureChanged with the root
   * as path, i.e. the whole tree has changed.
   *
   * @param previousRoot the previous  root
   */
  protected void fireRootTreeStructureChanged( @Nonnull Object previousRoot ) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
      if ( listeners[i] == TreeModelListener.class ) {
        // Lazily toInTray the event:
        if ( e == null ) {
          e = new TreeModelEvent( this, new Object[]{previousRoot} );
        }
        ( ( TreeModelListener ) listeners[i + 1] ).treeStructureChanged( e );
      }
    }
  }

  protected void fireTreeNodeRemoved( Object[] path, int index, Object child ) {
    fireTreeNodesRemoved( path, new int[]{index}, new Object[]{child} );
  }

  protected void fireTreeNodeInserted( Object[] path, int index, Object child ) {
    fireTreeNodesInserted( path, new int[]{index}, new Object[]{child} );
  }

  protected void fireTreeStructureChanged( Object[] path, int index, Object child ) {
    fireTreeStructureChanged( path, new int[]{index}, new Object[]{child} );
  }

  protected void fireTreeNodeChanged( Object[] path, int index, Object child ) {
    fireTreeNodesChanged( path, new int[]{index}, new Object[]{child} );
  }
}
