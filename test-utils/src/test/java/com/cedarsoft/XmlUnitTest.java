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

import org.custommonkey.xmlunit.XMLAssert;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * <p>XmlUnitTest class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XmlUnitTest {
  @NotNull
  @NonNls
  public static final String WITH_WHITESPACES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
    "</fileType>";
  @NotNull
  @NonNls
  public static final String WITH_WHITESPACES_DIFFERENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">jpg</extension>\n" +
    "</fileType>";
  @NotNull
  @NonNls
  public static final String WITH_WHITESPACES_SINGLE_QUOTED = "<?xml version='1.0' encoding='UTF-8'?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
    "</fileType>";
  @NotNull
  @NonNls
  public static final String WITHOUT_WHITESPACES = "<?xml version='1.0' encoding='UTF-8'?><fileType dependent=\"false\"><id>Canon Raw</id><extension default=\"true\" delimiter=\".\">cr2</extension></fileType>";

  /**
   * <p>testIt</p>
   *
   * @throws IOException  if any.
   * @throws SAXException if any.
   */
  @Test
  public void testIt() throws IOException, SAXException {
    XMLAssert.assertXMLEqual( WITH_WHITESPACES_SINGLE_QUOTED,
                              WITH_WHITESPACES
    );
    AssertUtils.assertXMLEquals( WITH_WHITESPACES_SINGLE_QUOTED,
                                 WITH_WHITESPACES
    );
  }

  /**
   * <p>testProblem</p>
   *
   * @throws IOException  if any.
   * @throws SAXException if any.
   */
  @Test
  public void testWhitespaces() throws IOException, SAXException {
    AssertUtils.setIgnoreWhitespace( true );
    XMLAssert.assertXMLEqual( WITHOUT_WHITESPACES,
                              WITH_WHITESPACES );
    AssertUtils.assertXMLEquals( WITHOUT_WHITESPACES,
                                 WITH_WHITESPACES );
  }

  @Test
  public void testEmpty() throws IOException, SAXException {
    try {
      AssertUtils.assertXMLEquals( "", "<xml/>" );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
    }
    try {
      AssertUtils.assertXMLEquals( "", "" );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
    }
    try {
      AssertUtils.assertXMLEquals( "<xml/>", "" );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
    }
  }
}
