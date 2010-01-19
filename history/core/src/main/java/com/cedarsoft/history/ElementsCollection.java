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

package com.cedarsoft.history;

import com.cedarsoft.lock.Lockable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains a collection of entries
 *
 * @param <E> the type
 */
public class ElementsCollection<E> implements ObservableObjectAccess<E>, Lockable {
  @NotNull
  @NonNls
  public static final String PROPERTY_ELEMENTS = "elements";

  private Long id;
  @NotNull
  protected final List<E> elements = new ArrayList<E>();
  @NotNull
  protected final CollectionSupport<E> collectionSupport = new CollectionSupport<E>( this );
  @NotNull
  protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  public ElementsCollection() {
  }

  public ElementsCollection( @NotNull Collection<? extends E> elements ) {
    this.elements.addAll( elements );
  }

  public ElementsCollection( @NotNull E... elements ) {
    this( Arrays.asList( elements ) );
  }

  @Override
  public final void add( @NotNull E element ) {
    addElement( element );
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

  @Override
  public void commit( @NotNull E element ) {
    lock.readLock().lock();
    int index;
    try {
      if ( !elements.contains( element ) ) {
        throw new IllegalArgumentException( "Invalid element commited: " + element + ". Is not contained within this collection." );
      }
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

  public boolean isEmpty() {
    return !hasElements();
  }

  public void addAll( @NotNull Iterable<? extends E> additionalElements ) {
    for ( E element : additionalElements ) {
      add( element );
    }
  }

  public int size() {
    lock.readLock().lock();
    try {
      return elements.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns all cessions
   *
   * @return the cessions
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

  /**
   * Sets the elements
   *
   * @param elements the elements
   */
  @Override
  public void setElements( @NotNull List<? extends E> elements ) {
    Collection<E> newElements = new ArrayList<E>( elements );

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

  @Override
  public final void remove( @NotNull E element ) {
    removeEntry( element );
  }

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

  @Override
  public void addElementListener( @NotNull ElementsListener<? super E> listener ) {
    collectionSupport.addElementListener( listener );
  }

  @Override
  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    collectionSupport.removeElementListener( listener );
  }

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
   *
   * @throws NoElementFoundException if no entry has been found
   */
  @NotNull
  public E findFirstElement( @NotNull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    E found = findFirstElementNullable( visitor );
    if ( found == null ) {
      throw new NoElementFoundException( "No element found for " + visitor.getIdentifier() );
    }
    return found;
  }

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

  public boolean contains( @NotNull E element ) {
    lock.readLock().lock();
    try {
      return elements.contains( element );
    } finally {
      lock.readLock().unlock();
    }
  }

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
   * @return the removed elements
   */
  @NotNull
  public List<? extends E> removeElements( @NotNull ElementVisitor<? super E> visitor ) {
    List<E> removed = new ArrayList<E>();
    lock.writeLock().lock();
    try {
      for ( int i = 0; i < elements.size(); ) {
        E element = elements.get( i );
        if ( visitor.fits( element ) ) {
          elements.remove( i );
          removed.add( element );
          collectionSupport.elementDeleted( element, i );
        } else {
          i++;
        }
      }
    } finally {
      lock.writeLock().unlock();
    }

    return removed;
  }

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

  @Override
  @NotNull
  public ReentrantReadWriteLock getLock() {
    return lock;
  }

  /**
   * XStream
   *
   * @return this
   */
  @NotNull
  private Object readResolve() {
    try {
      {
        Field field = ElementsCollection.class.getDeclaredField( "collectionSupport" );
        field.setAccessible( true );
        field.set( this, new CollectionSupport<E>( this ) );
      }
      {
        Field field = ElementsCollection.class.getDeclaredField( "lock" );
        field.setAccessible( true );
        field.set( this, new ReentrantReadWriteLock() );
      }
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
    return this;
  }
}
