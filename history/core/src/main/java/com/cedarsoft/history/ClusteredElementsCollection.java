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

package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains a collection of entries
 *
 * @param <E> the type
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ClusteredElementsCollection<E> implements ClusteredObservableObjectAccess<E> {
  /** Constant <code>PROPERTY_ELEMENTS="elements"</code> */
  @NotNull
  @NonNls
  public static final String PROPERTY_ELEMENTS = "elements";

  private Long id;
  @NotNull
  protected final List<E> elements = new ArrayList<E>();

  @NotNull
  protected final ClusteredCollectionSupport<E> collectionSupport = new ClusteredCollectionSupport<E>( this );

  @NotNull
  protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * <p>Constructor for ClusteredElementsCollection.</p>
   */
  public ClusteredElementsCollection() {
  }

  /**
   * <p>Constructor for ClusteredElementsCollection.</p>
   *
   * @param elements a {@link java.util.Collection} object.
   */
  public ClusteredElementsCollection( @NotNull Collection<? extends E> elements ) {
    this.elements.addAll( elements );
  }

  /** {@inheritDoc} */
  @Override
  public final void add( @NotNull E element ) {
    addElement( element );
  }

  /**
   * <p>Getter for the field <code>collectionSupport</code>.</p>
   *
   * @return a {@link com.cedarsoft.history.ClusteredCollectionSupport} object.
   */
  @Deprecated
  @NotNull
  public ClusteredCollectionSupport<E> getCollectionSupport() {
    return collectionSupport;
  }

  /**
   * Adds a new entry
   *
   * @param element the entry that is added
   */
  public void addElement( @NotNull E element ) {
    lock.writeLock().lock();
    int index;
    try {
      elements.add( element );
      index = elements.indexOf( element );
    } finally {
      lock.writeLock().unlock();
    }
    collectionSupport.elementAdded( element, index );
  }

  /** {@inheritDoc} */
  @Override
  public void commit( @NotNull E element ) {

    lock.readLock().lock();
    int index;
    try {
      index = elements.indexOf( element );
    } finally {
      lock.readLock().unlock();
    }

    collectionSupport.elementChanged( element, index );
  }

  /**
   * Whether this contains any entries
   *
   * @return whether this  contains any entries
   */
  public boolean hasElements() {
    lock.readLock().lock();
    try {
      return !elements.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>isEmpty</p>
   *
   * @return a boolean.
   */
  public boolean isEmpty() {
    return !hasElements();
  }

  /**
   * <p>addAll</p>
   *
   * @param additionalElements a {@link java.util.List} object.
   */
  public void addAll( @NotNull List<? extends E> additionalElements ) {
    for ( E element : additionalElements ) {
      add( element );
    }
  }

  /**
   * <p>size</p>
   *
   * @return a int.
   */
  public int size() {
    lock.readLock().lock();
    try {
      return elements.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   *
   * Returns all cessions
   */
  @Override
  @NotNull
  public List<? extends E> getElements() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( elements );
    } finally {
      lock.readLock().unlock();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setElements( @NotNull List<? extends E> elements ) {
    List<E> newElements = new ArrayList<E>( elements );

    lock.writeLock().lock();
    try {
      for ( E element : new ArrayList<E>( this.elements ) ) {
        if ( !newElements.remove( element ) ) {
          remove( element );
        }
      }

      for ( E newElement : newElements ) {
        add( newElement );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /** {@inheritDoc} */
  @Override
  public final void remove( @NotNull E element ) {
    removeEntry( element );
  }

  /**
   * <p>removeEntry</p>
   *
   * @param element a E object.
   * @return a boolean.
   */
  public boolean removeEntry( @NotNull E element ) {
    lock.writeLock().lock();
    boolean removed;
    int index;
    try {
      index = elements.indexOf( element );
      removed = elements.remove( element );
    } finally {
      lock.writeLock().unlock();
    }

    collectionSupport.elementDeleted( element, index );
    return removed;
  }

  /** {@inheritDoc} */
  @Override
  public void addElementListener( @NotNull ElementsListener<? super E> listener ) {
    addElementListener( listener, true );
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return collectionSupport.getTransientElementListeners();
  }

  /** {@inheritDoc} */
  @Override
  public void addElementListener( @NotNull ElementsListener<? super E> listener, boolean isTransient ) {
    collectionSupport.addElementListener( listener, isTransient );
  }

  /** {@inheritDoc} */
  @Override
  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    collectionSupport.removeElementListener( listener );
  }

  /**
   * <p>findElements</p>
   *
   * @param visitor a {@link com.cedarsoft.history.ElementVisitor} object.
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends E> findElements( @NotNull ElementVisitor<? super E> visitor ) {
    List<E> found = new ArrayList<E>();

    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          found.add( element );
        }
      }
    } finally {
      lock.readLock().unlock();
    }

    return Collections.unmodifiableList( found );
  }

  /**
   * Returns the first entry that matches the visistor
   *
   * @param visitor the visitor that identifies the entries
   * @return the first entry
   * @throws com.cedarsoft.history.NoElementFoundException if no entry has been found
   */
  @NotNull
  public E findFirstElement( @NotNull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    E found = findFirstElementNullable( visitor );
    if ( found == null ) {
      throw new NoElementFoundException( "No element found for " + visitor.getIdentifier() );
    }
    return found;
  }

  /**
   * <p>findFirstElementNullable</p>
   *
   * @param visitor a {@link com.cedarsoft.history.ElementVisitor} object.
   * @return a E object.
   */
  @Nullable
  public E findFirstElementNullable( @NotNull ElementVisitor<? super E> visitor ) {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return element;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return null;
  }

  /**
   * <p>contains</p>
   *
   * @param element a E object.
   * @return a boolean.
   */
  public boolean contains( @NotNull E element ) {
    lock.readLock().lock();
    try {
      return elements.contains( element );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>contains</p>
   *
   * @param visitor a {@link com.cedarsoft.history.ElementVisitor} object.
   * @return a boolean.
   * @throws com.cedarsoft.history.NoElementFoundException if any.
   */
  public boolean contains( @NotNull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return true;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return false;
  }

  /**
   * Removes the entries
   *
   * @param visitor the visitor that describes the entries
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<? extends E> removeElements( @NotNull ElementVisitor<? super E> visitor ) {
    List<E> removed = new ArrayList<E>();
    lock.writeLock().lock();
    try {
      for ( Iterator<E> it = elements.iterator(); it.hasNext(); ) {
        E element = it.next();
        if ( visitor.fits( element ) ) {
          it.remove();
          removed.add( element );
        }
      }
    } finally {
      lock.writeLock().unlock();
    }

    return removed;
  }

  /**
   * <p>clear</p>
   */
  public void clear() {
    if ( !collectionSupport.hasListeners() ) {
      lock.writeLock().lock();
      try {
        elements.clear();
      } finally {
        lock.writeLock().unlock();
      }
    }

    lock.writeLock().lock();
    try {
      for ( Iterator<E> it = elements.iterator(); it.hasNext(); ) {
        E element = it.next();
        it.remove();
        collectionSupport.elementDeleted( element, 0 );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public ReentrantReadWriteLock getLock() {
    return lock;
  }
}
