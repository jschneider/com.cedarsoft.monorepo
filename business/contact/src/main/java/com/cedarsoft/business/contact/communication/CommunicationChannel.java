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
 * Describes a communication channel (e.g. phone, mail...)
 */
public interface CommunicationChannel {

  @Nonnull
  String PROPERTY_REPRESENTATION = "representation";

  @Nonnull
  String PROPERTY_DESCRIPTION = "description";

  @Nonnull
  String PROPERTY_CLASSIFICATION = "classification";

  @Nonnull
  String PROPERTY_ACTIVE = "active";

  /**
   * Returns the classification
   *
   * @return the classification
   */
  @Nonnull
  Classification getClassification();

  /**
   * Returns a string that represents the communication channel
   *
   * @return a string representation
   */
  @Nonnull

  String getRepresentation();

  @Nonnull
  String getDescription();

  boolean isActive();

  void setActive( boolean active );

  /**
   * Returns the classification
   *
   * @param classification the classification
   */
  void setClassification( @Nonnull Classification classification );
}