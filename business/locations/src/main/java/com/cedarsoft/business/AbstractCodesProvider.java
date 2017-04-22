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

package com.cedarsoft.business;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.Collections;

/**
 * Abstract implementation of a code provider.
 */
public abstract class AbstractCodesProvider {
  @Nonnull
  protected final Multimap<String, String> name2codes = HashMultimap.create();
  @Nonnull
  protected final Multimap<String, String> code2names = HashMultimap.create();

  /**
   * Returns the codes for the given name
   *
   * @param name the name
   * @return a collection of codes
   */
  @Nonnull

  public Collection<String> getCodes( @Nonnull  String name ) {
    Collection<String> postalCodes = name2codes.get( name );
    if ( postalCodes == null || postalCodes.isEmpty() ) {
      throw new IllegalArgumentException( "No postal codes found for " + name );
    }
    return Collections.unmodifiableCollection( postalCodes );
  }

  /**
   * Returns the names for the given code
   *
   * @param code the code
   * @return the names
   */
  @Nonnull

  public Collection<String> getNames( @Nonnull  String code ) {
    Collection<String> cityNames = code2names.get( code );
    if ( cityNames == null || cityNames.isEmpty() ) {
      throw new IllegalArgumentException( "No city found for " + code );
    }
    return cityNames;
  }
}
