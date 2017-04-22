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
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.business.contact.communication.CommunicationChannel;
import com.cedarsoft.business.contact.communication.Email;
import com.cedarsoft.business.contact.communication.FaxNumber;
import com.cedarsoft.business.contact.communication.PhoneNumber;
import com.cedarsoft.cluster.event.ClusteredPropertyChangeSupport;
import com.cedarsoft.history.core.ElementVisitor;
import com.cedarsoft.history.core.ElementsListener;

/**
 * A contact
 */
public class Contact {
  @Nonnull

  public static final String PROPERTY_ADDRESSES = "addresses";
  /**
   * Property for the first address
   */
  @Nonnull

  public static final String PROPERTY_ADDRESS = "address";
  @Nonnull

  public static final String PROPERTY_ID = "id";
  @Nonnull

  public static final String PROPERTY_TYPE = "type";
  @Nonnull

  public static final String PROPERTY_OTHER_COMMUNICATION_CHANNELS = "otherCommunicationChannels";
  @Nonnull

  public static final String PROPERTY_BANK_DETAILS = "bankDetails";


  private Long id;

  @Nonnull
  protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  @Nonnull
  protected final ClusteredPropertyChangeSupport pcs = new ClusteredPropertyChangeSupport(this);
  @Nonnull
  private final BankDetailsList bankDetails = new BankDetailsList();
  @Nonnull
  private final CommunicationChannels otherCommunicationChannels = new CommunicationChannels();
  @Nonnull
  private final Addresses addresses = new Addresses();

  public void addBankDetails(@Nonnull BankDetails additionalBankDetails) {
    bankDetails.addElement(additionalBankDetails);
  }

  @SuppressWarnings({"ParameterHidesMemberVariable"})
  public void removeBankDetails(BankDetails bankDetails) {
    this.bankDetails.removeEntry(bankDetails);
  }

  public void addCommunicationChannel(@Nonnull CommunicationChannel communicationChannel) {
    otherCommunicationChannels.addElement(communicationChannel);
  }

  public void removeCommunicationChannel(@Nonnull CommunicationChannel communicationChannel) {
    otherCommunicationChannels.removeEntry(communicationChannel);
  }

  @Deprecated
  public void removeAddress(@Nonnull BaseAddress address) {
    addresses.removeEntry(address);
  }

  @Nonnull
  public Addresses getAddresses() {
    return addresses;
  }

  @Nonnull
  public CommunicationChannels getOtherCommunicationChannels() {
    return otherCommunicationChannels;
  }

  public void addAddress(@Nonnull BaseAddress address) {
    addresses.addElement(address);
  }

  @Nonnull
  public BaseAddress getAddress() {
    addresses.getLock().writeLock().lock();
    try {
      if (addresses.isEmpty()) {
        Address address = new Address();
        address.setActive(true);
        addAddress(address);
      }
      return addresses.getElements().get(0);
    } finally {
      addresses.getLock().writeLock().unlock();
    }
  }

  @Nonnull
  public FaxNumber getFax() throws IllegalStateException {
    return otherCommunicationChannels.getFirstActiveFax();
  }

  @Nonnull
  public Email getMail() throws IllegalStateException {
    return otherCommunicationChannels.getFirstActiveMail();
  }

  @Nonnull
  public PhoneNumber getPhoneNumber() throws IllegalStateException {
    return otherCommunicationChannels.getFirstActivePhoneNumber();
  }

  /**
   * Returns all communication channels
   *
   * @return the communication channels
   */
  @Nonnull
  public List<? extends CommunicationChannel> getCommunicationChannels() {
    return getOtherCommunicationChannels().getElements();
  }

  @Nonnull
  public BankDetailsList getBankDetails() {
    return bankDetails;
  }

  @Nullable
  public BaseAddress getFirstActiveAddress() {
    return addresses.findFirstElementNullable(new ElementVisitor<BaseAddress>("first active address") {
      @Override
      public boolean fits(@Nonnull BaseAddress element) {
        return element.isActive();
      }
    });
  }

  public void addAddressListener(@Nonnull ElementsListener<BaseAddress> listener) {
    addresses.addElementListener(listener);
  }

  @Nullable
  public Long getId() {
    return id;
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
}
