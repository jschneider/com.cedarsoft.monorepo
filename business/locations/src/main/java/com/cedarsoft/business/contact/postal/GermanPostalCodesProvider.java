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

package com.cedarsoft.business.contact.postal;

import com.cedarsoft.business.AbstractCodesProvider;
import com.cedarsoft.business.contact.City;
import com.cedarsoft.business.contact.CityCodeProvider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Collection;


/**
 * A special class that is able to parse German postal codes
 */
public class GermanPostalCodesProvider extends AbstractCodesProvider implements CityCodeProvider {

  private static final String RESOURCE_NAME_DE_PLZ_TXT = "de_plz.csv";

  public GermanPostalCodesProvider() throws IOException {
    URL resource = getClass().getResource( RESOURCE_NAME_DE_PLZ_TXT );
    if ( resource == null ) {
      throw new IOException( "Resource \"" + RESOURCE_NAME_DE_PLZ_TXT + "\" not found" );
    }

    LineIterator iterator = IOUtils.lineIterator( resource.openStream(), "UTF-8" );
    while ( iterator.hasNext() ) {
      String line = iterator.nextLine();
      String[] parts = line.split( "\t" );
      if ( parts.length == 0 ) {
        continue;
      }

      code2names.put( parts[0], parts[1] );
      name2codes.put( parts[1], parts[0] );
    }
  }

  /**
   * Returns the first city for the given postal code
   *
   * @param postalCode the postal code
   * @return the first city for the given postal code
   */
  @Nonnull
  public City getCity( @Nonnull  String postalCode ) {
    String cityName = getCityNames( postalCode ).iterator().next();
    return new City( postalCode, cityName );
  }

  @Nonnull

  public Collection<String> getCityNames( @Nonnull  String code ) {
    return getNames( code );
  }

  @Nonnull

  public String getCountryCode() {
    return Locale.GERMANY.getCountry();
  }
}