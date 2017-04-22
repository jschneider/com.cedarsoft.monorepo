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

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Contains several city code providers.
 */
public class CityCodeProvidersRegistry {
  /**
   * The key is the ISO2 for the country
   */
  @Nonnull
  private final Map<String, CityCodeProvider> codeProviders = new HashMap<String, CityCodeProvider>();


  /**
   * Adds an additional provider
   *
   * @param provider the provider that is registered
   */
  public void add( @Nonnull CityCodeProvider provider ) {
    codeProviders.put( provider.getCountryCode(), provider );
  }

  /**
   * Returns the provider for the given locale
   *
   * @param locale the locale
   * @return the city code provider
   */
  @Nonnull
  public CityCodeProvider getProvider( @Nonnull Locale locale ) {
    CityCodeProvider provider = codeProviders.get( locale.getCountry() );
    if ( provider == null ) {
      throw new IllegalArgumentException( "No provider registered for locale " + locale );
    }
    return provider;
  }
}
