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

import com.cedarsoft.business.contact.postal.GermanPostalCodesProvider;

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 */
public class PlzTest {
  private CityCodeProvidersRegistry registry;
  private GermanPostalCodesProvider provider;

  @Before
  public void setUp() throws Exception {
    registry = new CityCodeProvidersRegistry();

    provider = new GermanPostalCodesProvider();
    registry.add( provider );
  }

  @Test
  public void testFind() {
    assertEquals( 31, registry.getProvider( Locale.GERMANY ).getCodes( "Dresden" ).size() );
    assertEquals( "72144", registry.getProvider( Locale.GERMANY ).getCodes( "Dußlingen" ).iterator().next() );
    assertEquals( "72810", registry.getProvider( Locale.GERMANY ).getCodes( "Gomaringen" ).iterator().next() );
  }

  @Test
  public void testReverse() {
    for ( String postalCode : provider.getCodes( "Tübingen" ) ) {
      assertEquals( postalCode, "Tübingen", provider.getCityNames( postalCode ).iterator().next() );
    }

    Collection<String> cityNames = provider.getCityNames( "01465" );
    assertEquals( 2, cityNames.size() );
    Iterator<String> iterator = cityNames.iterator();
    assertEquals( "Dresden", iterator.next() );
    assertEquals( "Langebrück", iterator.next() );
  }

  @Test
  public void testGetCity() {
    assertEquals( new City( "72144", "Dußlingen" ), provider.getCity( "72144" ) );
  }

  @Test
  public void testProviders() {
    assertEquals( Locale.GERMANY.getCountry(), provider.getCountryCode() );
    assertEquals( "DE", provider.getCountryCode() );
    assertNotNull( Country.findCountry( provider.getCountryCode() ) );

    registry.add( new CityCodeProvider() {
      @Nonnull

      public Collection<String> getCityNames( @Nonnull  String code ) {
        throw new UnsupportedOperationException();
      }

      @Nonnull

      public Collection<String> getCodes( @Nonnull  String city ) {
        throw new UnsupportedOperationException();
      }

      @Nonnull

      public String getCountryCode() {
        return "FR";
      }
    } );

    assertSame( provider, registry.getProvider( Locale.GERMANY ) );
    assertNotNull( registry.getProvider( Locale.FRANCE ) );
  }
}
