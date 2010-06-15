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

package com.cedarsoft;

import org.apache.commons.lang.ObjectUtils;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;

/**
 * Compares property changes
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class PropertyChangeEventMatcher implements IArgumentMatcher {
  /**
   * <p>create</p>
   *
   * @param event a {@link java.beans.PropertyChangeEvent} object.
   * @return a {@link java.beans.PropertyChangeEvent} object.
   */
  @Nullable
  public static PropertyChangeEvent create( @Nullable PropertyChangeEvent event ) {
    EasyMock.reportMatcher( new PropertyChangeEventMatcher( event ) );
    return event;
  }

  @Nullable
  private final PropertyChangeEvent expected;

  /**
   * <p>Constructor for PropertyChangeEventMatcher.</p>
   *
   * @param expected a {@link java.beans.PropertyChangeEvent} object.
   */
  public PropertyChangeEventMatcher( @Nullable PropertyChangeEvent expected ) {
    this.expected = expected;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches( Object argument ) {
    if ( ObjectUtils.equals( expected, argument ) ) {
      return true;
    }

    if ( expected == null ) {
      return false;
    }

    if ( !( argument instanceof PropertyChangeEvent ) ) {
      return false;
    }

    PropertyChangeEvent actual = ( PropertyChangeEvent ) argument;
    return ObjectUtils.equals( actual.getNewValue(), expected.getNewValue() ) &&
      ObjectUtils.equals( actual.getPropertyName(), expected.getPropertyName() ) &&
      ObjectUtils.equals( actual.getOldValue(), expected.getOldValue() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void appendTo( StringBuffer buffer ) {
    buffer.append( "PropertyChangeEvent did not fit: Expected <" + expected + ">" );
  }
}
