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
package com.cedarsoft.business.payment;

import com.cedarsoft.business.Money;
import org.joda.time.LocalDate;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class DefaultPayment implements Payment {
  @Nonnull

  public static final String PROPERTY_DESCRIPTION = "description";
  @Nonnull

  public static final String PROPERTY_AMOUNT = "amount";
  @Nonnull

  public static final String PROPERTY_AMOUNT_NEGATIVE = "amountNegative";
  @Nonnull

  public static final String PROPERTY_DATE = "date";

  @Nonnull
  protected ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);


  @Deprecated
  private Long id;
  @Nonnull
  private String description;
  @Nonnull
  private Money amount;
  @Nonnull
  private LocalDate date;

  public DefaultPayment(@Nonnull Money amount, @Nonnull LocalDate date) {
    this(amount, date, "");
  }

  public DefaultPayment(@Nonnull Money amount, @Nonnull LocalDate date, @Nonnull String description) {
    this.amount = amount;
    this.date = date;
    this.description = description;
  }

  @Nonnull
  @Override
  public Money getAmount() {
    lock.readLock().lock();
    try {
      return amount;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nonnull
  public Money getAmountNegative() {
    return getAmount().negative();
  }

  @Override
  @Nonnull
  public LocalDate getDate() {
    lock.readLock().lock();
    try {
      return date;
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

  public void setDescription(@Nonnull String description) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_DESCRIPTION, this.description, this.description = description);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setAmount(@Nonnull Money amount) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_AMOUNT, this.amount, this.amount = amount);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void setAmountNegative(@Nonnull Money amountNegative) {
    setAmount(amountNegative.negative());
  }

  public void setDate(@Nonnull LocalDate date) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange(PROPERTY_DATE, this.date, this.date = date);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public int compareTo(@Nonnull Payment o) {
    return getDate().compareTo(o.getDate());
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

  public void addPropertyChangeListener(@Nonnull String propertyName, @Nonnull PropertyChangeListener listener) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener(propertyName, listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public String toString() {
    lock.readLock().lock();
    try {
      return "DefaultPayment{" +
        "amount=" + amount +
        ", date=" + date +
        ", description='" + description + '\'' +
        '}';
    } finally {
      lock.readLock().unlock();
    }
  }
}
