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

package com.cedarsoft.business.contact;

import com.cedarsoft.business.contact.communication.CommunicationChannel;
import com.cedarsoft.business.contact.communication.Email;
import com.cedarsoft.business.contact.communication.FaxNumber;
import com.cedarsoft.business.contact.communication.PhoneNumber;
import com.cedarsoft.history.core.ClusteredElementsCollection;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommunicationChannels extends ClusteredElementsCollection<CommunicationChannel> {
  @Nonnull

  public static final String PROPERTY_FIRST_ACTIVE_FAX = "firstActiveFax";
  @Nonnull

  public static final String PROPERTY_FIRST_ACTIVE_MAIL = "firstActiveMail";
  @Nonnull

  public static final String PROPERTY_FIRST_ACTIVE_PHONE_NUMBER = "getFirstActivePhoneNumber";

  @Nonnull
  private static final Map<Class<? extends CommunicationChannel>, Integer> sorting = new HashMap<Class<? extends CommunicationChannel>, Integer>();

  static {
    sorting.put( Address.class, 5 );
    sorting.put( POBAddress.class, 6 );
    sorting.put( PhoneNumber.class, 10 );
    sorting.put( FaxNumber.class, 20 );
    sorting.put( Email.class, 30 );
  }

  @Override
  public void addElement( @Nonnull CommunicationChannel element ) {
    lock.writeLock().lock();
    int index;
    try {
      elements.add( element );
      Collections.sort( elements, new Comparator<CommunicationChannel>() {
        public int compare( CommunicationChannel o1, CommunicationChannel o2 ) {
          return sorting.get( o1.getClass() ).compareTo( sorting.get( o2.getClass() ) );
        }
      } );

      index = elements.indexOf( element );
    } finally {
      lock.writeLock().unlock();
    }
    collectionSupport.elementAdded( element, index );
  }


  /**
   * Returns the first active fax (or creates a new one if necessary)
   *
   * @return the first active fax
   *
   * @throws IllegalStateException
   */
  @Nonnull
  public FaxNumber getFirstActiveFax() throws IllegalStateException {
    try {
      return findFirstActiveChannel( FaxNumber.class );
    } catch ( IllegalStateException ignore ) {
    }

    FaxNumber fax = new FaxNumber( "??", "?????", "?????" );
    fax.setActive( true );
    addElement( fax );

    return fax;
  }

  @Nonnull
  public Email getFirstActiveMail() {
    try {
      return findFirstActiveChannel( Email.class );
    } catch ( IllegalStateException ignore ) {
    }

    Email newChannel = new Email( "?????@????.com" );
    newChannel.setActive( true );
    addElement( newChannel );

    return newChannel;
  }

  @Nonnull
  public PhoneNumber getFirstActivePhoneNumber() {
    try {
      return findFirstActiveChannel( PhoneNumber.class );
    } catch ( IllegalStateException ignore ) {
    }

    PhoneNumber newChannel = new PhoneNumber( "??", "?????", "?????" );
    newChannel.setActive( true );
    addElement( newChannel );

    return newChannel;
  }

  /**
   * Returns the first active fax
   *
   * @return the first active fax
   *
   * @throws IllegalStateException if no fax could be found
   */
  @Nonnull
  public <T extends CommunicationChannel> T findFirstActiveChannel( @Nonnull Class<T> type ) throws IllegalStateException {
    lock.readLock().lock();
    try {
      for ( CommunicationChannel entry : elements ) {
        if ( !entry.isActive() ) {
          continue;
        }
        if ( type.isAssignableFrom( entry.getClass() ) ) {
          return type.cast( entry );
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    throw new IllegalStateException( "No active communication channel found with type " + type.getName() );
  }
}
