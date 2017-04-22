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

import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.*;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 */
public class CountryTest {
  @Before
  public void setUp() throws Exception {
    ResourceBundle.clearCache();
    Locale.setDefault( Locale.GERMANY );
  }

  @Test
  public void testCrazyDefaultLocale() {
    Locale.setDefault( Locale.JAPAN );
    assertNotNull( Country.findCountry( "DE" ) );
    assertEquals( "Germany", Country.Germany.getName() );
  }

  @Test
  public void testI18n() {
    ResourceBundle defaultBundle = ResourceBundle.getBundle( Country.COUNTRY_NAMES_BUNDLE_NAME, Locale.US );
    assertEquals( "Germany", defaultBundle.getString( "DE" ) );
    ResourceBundle germanBundle = ResourceBundle.getBundle( Country.COUNTRY_NAMES_BUNDLE_NAME, Locale.GERMANY );
    assertEquals( "Deutschland", germanBundle.getString( "DE" ) );
    ResourceBundle.clearCache();

    assertNotSame( defaultBundle, germanBundle );
    assertEquals( "Germany", defaultBundle.getString( "DE" ) );
    assertEquals( "Deutschland", germanBundle.getString( "DE" ) );

    assertEquals( "Germany", Country.Germany.getName( Locale.US ) );
    assertEquals( "Deutschland", Country.Germany.getName( Locale.GERMANY ) );
  }

  @Test
  public void testResolve() {
    assertNotNull( Country.findCountry( "DE" ) );
    assertNotNull( Country.findCountry( "de" ) );

    assertEquals( "DE", Country.findCountryWithName( "Deutschland" ).getIsoCode() );
  }

  @Test
  public void testLocale2() {
    assertEquals( "Deutschland", Country.findCountry( Locale.GERMANY.getCountry() ).getName() );

    Locale locale = Country.findCountry( Locale.GERMANY.getCountry() ).getLocale();
    assertNotNull( locale );
    assertEquals( "DE", locale.getCountry() );

    assertEquals( Locale.GERMANY, Country.findCountry( Locale.GERMANY.getCountry() ).getLocale() );
  }

  @Test
  public void testLocale() {
    Locale locale = new Locale( "es", "MX" );

    Country country = Country.findCountry( locale.getCountry() );
    Assert.assertEquals( "MX", country.getIsoCode() );
    Assert.assertEquals( "Mexiko", country.getName() );
  }

  @Test
  public void testGetDefault() {
    assertNotNull( Country.getDefaultCountry() );
    Assert.assertEquals( "DE", Country.getDefaultCountry().getIsoCode() );
    Assert.assertEquals( "Deutschland", Country.getDefaultCountry().getName() );

    Locale.setDefault( Locale.US );
    assertNotNull( Country.getDefaultCountry() );
    Assert.assertEquals( "US", Country.getDefaultCountry().getIsoCode() );
    Assert.assertEquals( "United States", Country.getDefaultCountry().getName() );

    Locale.setDefault( Locale.GERMANY );
    Assert.assertEquals( "DE", Country.getDefaultCountry().getIsoCode() );
    Assert.assertEquals( "Deutschland", Country.getDefaultCountry().getName() );
  }
}
