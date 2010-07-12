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

import com.cedarsoft.xml.XmlCommons;
import junit.framework.AssertionFailedError;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>AssertUtils class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class AssertUtils {
  private AssertUtils() {
  }

  /**
   * <p>setIgnoreWhitespace</p>
   *
   * @param ignore a boolean.
   */
  public static void setIgnoreWhitespace( boolean ignore ) {
    XMLUnit.setIgnoreWhitespace( ignore );
  }

  /**
   * <p>assertXMLEqual</p>
   *
   * @param control a {@link String} object.
   * @param test    a {@link String} object.
   * @throws SAXException if any.
   * @throws IOException  if any.
   */
  public static void assertXMLEquals( String control, String test ) throws SAXException, IOException {
    assertXMLEquals( control, test, true );
  }

  public static void assertXMLEquals( URL control, String test ) throws SAXException, IOException {
    assertXMLEquals( toString( control ), test );
  }

  /**
   * <p>assertXMLEqual</p>
   *
   * @param control          a {@link String} object.
   * @param test             a {@link String} object.
   * @param ignoreWhiteSpace a boolean.
   * @throws SAXException if any.
   * @throws IOException  if any.
   */
  public static void assertXMLEquals( String control, String test, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    assertXMLEquals( null, control, test, ignoreWhiteSpace );
  }

  public static void assertXMLEquals( URL control, String test, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    assertXMLEquals( toString( control ), test, ignoreWhiteSpace );
  }

  /**
   * <p>assertXMLEqual</p>
   *
   * @param err              a {@link String} object.
   * @param control          a {@link String} object.
   * @param test             a {@link String} object.
   * @param ignoreWhiteSpace a boolean.
   * @throws SAXException if any.
   * @throws IOException  if any.
   */
  public static void assertXMLEquals( @Nullable @NonNls String err, @NotNull @NonNls String control, @NotNull @NonNls String test, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    if ( test.trim().length() == 0 ) {
      throw new AssertionError( "expected:<" + format( control ).trim() + "> but was:<" + test + '>' );
    }
    if ( control.trim().length() == 0 ) {
      throw new AssertionError( "expected:<" + control + "> but was:<" + format( test ).trim() + '>' );
    }

    try {
      setIgnoreWhitespace( ignoreWhiteSpace );
      XMLAssert.assertXMLEqual( err, test, control );
      setIgnoreWhitespace( false );
    } catch ( AssertionFailedError e ) {
      throw new AssertionError( "expected:<" + format( control ).trim() + "> but was:<" + format( test ).trim() + '>' );
    }
  }

  @NotNull
  @NonNls
  private static String format( @NotNull @NonNls String control ) {
    try {
      return XmlCommons.format( control );
    } catch ( Exception ignore ) {
      //Do not format if it is not possible...
      return control;
    }
  }

  public static void assertXMLEquals( String test, String err, @NotNull URL control, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    assertXMLEquals( err, toString( control ), test, ignoreWhiteSpace );
  }

  /**
   * <p>assertOne</p>
   *
   * @param current              a {@link Object} object.
   * @param expectedAlternatives a {@link Object} object.
   */
  public static void assertOne( @Nullable Object current, @NotNull Object... expectedAlternatives ) {
    List<AssertionError> failed = new ArrayList<AssertionError>();

    for ( Object expectedAlternative : expectedAlternatives ) {
      try {
        Assert.assertEquals( current, expectedAlternative );
        return; //Successfully
      } catch ( AssertionError e ) {
        failed.add( e );
      }
    }

    StringBuilder message = new StringBuilder();

    for ( AssertionError assertionError : failed ) {
      message.append( assertionError.getMessage() );
      message.append( "\n" );
    }

    throw new AssertionError( message.toString() );
  }

  /**
   * <p>assertEquals</p>
   *
   * @param expectedResourceUri a {@link java.net.URL} object.
   * @param actual              a {@link Object} object.
   * @throws IOException if any.
   */
  public static void assertEquals( @NotNull URL expectedResourceUri, @Nullable Object actual ) throws IOException {
    Assert.assertEquals( toString( expectedResourceUri ), actual );
  }

  @NotNull
  private static String toString( @NotNull URL expectedResourceUri ) throws IOException {
    return IOUtils.toString( expectedResourceUri.openStream() );
  }
}
