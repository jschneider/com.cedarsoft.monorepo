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

package com.cedarsoft.business.contact.communication;

import com.cedarsoft.business.contact.Classification;
import com.cedarsoft.cluster.event.ClusteredPropertyChangeSupport;

import javax.annotation.Nonnull;

import java.beans.PropertyChangeListener;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public abstract class AbstractCommunicationChannel implements CommunicationChannel, Comparable<CommunicationChannel> {
  @Nonnull
  protected ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  protected final ClusteredPropertyChangeSupport pcs = new ClusteredPropertyChangeSupport(this );

  private Long id;

  protected Classification classification = Classification.PRIVATE;
  @Nonnull
  protected String description = "";

  protected boolean active;

  protected AbstractCommunicationChannel() {
    this( Classification.PRIVATE, false );
  }

  protected AbstractCommunicationChannel( @Nonnull Classification classification, boolean active ) {
    this.classification = classification;
    this.active = active;
  }

  @Nonnull
  public Classification getClassification() {
    lock.readLock().lock();
    try {
      return classification;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setClassification( @Nonnull Classification classification ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_CLASSIFICATION, this.classification, this.classification = classification );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nonnull
  public String getDescription() {
    lock.readLock().lock();
    try {
      return description;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setDescription( @Nonnull String description ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_DESCRIPTION, this.description, this.description = description );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public boolean isActive() {
    lock.readLock().lock();
    try {
      return active;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setActive( boolean active ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_ACTIVE, this.active, this.active = active );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removePropertyChangeListener( @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removePropertyChangeListener( @Nonnull  String propertyName, @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @Nonnull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @Nonnull  String propertyName, @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener( @Nonnull  String propertyName, @Nonnull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public int compareTo( CommunicationChannel o ) {
    lock.readLock().lock();
    try {
      return toString().compareTo( o.toString() );
    } finally {
      lock.readLock().unlock();
    }
  }
}
