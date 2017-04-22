/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.business.contact;

import java.beans.PropertyChangeListener;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import com.cedarsoft.business.Bank;
import com.cedarsoft.cluster.event.ClusteredPropertyChangeSupport;

/**
 *
 */
public class BankDetails {

  @Nonnull
  public static final String PROPERTY_ACCOUNT_NUMBER = "accountNumber";
  @Nonnull
  public static final String PROPERTY_BANK = "bank";

  @Nonnull
  public static final String PROPERTY_DESCRIPTION = "description";
  @Nonnull
  public static final String PROPERTY_ACTIVE = "active";

  @Nonnull
  private ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  private final ClusteredPropertyChangeSupport pcs = new ClusteredPropertyChangeSupport(this);
  private Long id;

  @Nonnull
  private String accountNumber = "";
  @Nonnull
  private Bank bank = Bank.NULL;
  @Nonnull
  private String description = "";

  private boolean active;

  public BankDetails() {
  }

  public BankDetails(@Nonnull String accountNumber, @Nonnull Bank bank) {
    this.accountNumber = accountNumber;
    this.bank = bank;
  }

  @Nonnull

  public String getAccountNumber() {
    lock.readLock().lock();
    try {
      return accountNumber;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nonnull
  public Bank getBank() {
    lock.readLock().lock();
    try {
      return bank;
    } finally {
      lock.readLock().unlock();
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

  public boolean isActive() {
    lock.readLock().lock();
    try {
      return active;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setDescription(@Nonnull String description) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_DESCRIPTION, this.description, this.description = description);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setActive(boolean active) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_ACTIVE, this.active, this.active = active);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setAccountNumber(@Nonnull String accountNumber) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_ACCOUNT_NUMBER, this.accountNumber, this.accountNumber = accountNumber);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setBank(@Nonnull Bank bank) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_BANK, this.bank, this.bank = bank);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removePropertyChangeListener(@Nonnull PropertyChangeListener listener) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener(listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removePropertyChangeListener(@Nonnull String propertyName, @Nonnull PropertyChangeListener listener) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener(propertyName, listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener(@Nonnull PropertyChangeListener listener) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener(listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener(@Nonnull PropertyChangeListener listener, boolean isTransient) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener(listener, isTransient);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener(@Nonnull String propertyName, @Nonnull PropertyChangeListener listener) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener(propertyName, listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void addPropertyChangeListener(@Nonnull String propertyName, @Nonnull PropertyChangeListener listener, boolean isTransient) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener(propertyName, listener, isTransient);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public String toString() {
    lock.readLock().lock();
    try {
      return "BankDetails{" +
        "id=" + id +
        ", accountNumber='" + accountNumber + '\'' +
        ", bank=" + bank +
        '}';
    } finally {
      lock.readLock().unlock();
    }
  }
}
