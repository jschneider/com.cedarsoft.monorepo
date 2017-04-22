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

import javax.annotation.Nonnull;

/**
 *
 */
public class Email extends AbstractCommunicationChannel {

  private static final long serialVersionUID = -3842741432978369800L;

  public static final String PROPERTY_MAIL = "mail";
  @Nonnull

  private String mail = "";

  /**
   * Hibernate constructor
   */
  @Deprecated
  protected Email() {
  }

  public Email( @Nonnull  String mail ) {
    this.mail = mail;
  }

  public Email( @Nonnull Classification classification, @Nonnull  String mail ) {
    this( classification, mail, false );
  }

  public Email( @Nonnull Classification classification, @Nonnull  String mail, boolean active ) {
    super( classification, active );
    this.mail = mail;
  }

  @Nonnull

  public String getMail() {
    lock.readLock().lock();
    try {
      return mail;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setMail( @Nonnull String mail ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_MAIL, this.mail, this.mail = mail );
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nonnull

  public String getRepresentation() {
    return getMail();
  }

  @Override
  public String toString() {
    return getMail();
  }
}
