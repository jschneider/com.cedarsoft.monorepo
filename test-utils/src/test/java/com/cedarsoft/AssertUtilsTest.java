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

import org.junit.*;
import org.junit.rules.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
public class AssertUtilsTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testXml() throws Exception {
    expectedException.expect( AssertionError.class );
    expectedException.expectMessage( "expected:<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<xml2 />> but was: <<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<xml />>" );

    AssertUtils.assertXMLEquals( "<xml2/>", "<xml/>" );
  }

  @Test
  public void testAssertWithResource() throws IOException {
    AssertUtils.assertEquals( getClass().getResource( "equals.txt" ), "the content of the file...\n" +
      "second line!\n" +
      "third line!" );

    try {
      AssertUtils.assertEquals( getClass().getResource( "equals.txt" ), "other!" );
      fail( "Where is the Exception" );
    } catch ( AssertionError ignore ) {
    }
  }

  @Test
  public void testXml2() throws Exception {
    expectedException.expect( AssertionError.class );
    expectedException.expectMessage( "expected:<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<xml2 />> but was: <<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<xml />>" );

    AssertUtils.assertXMLEquals( getClass().getResource( "AssertUtilsTest.1.xml" ), "<xml/>" );
  }

  @Test
  public void testXml2WhiteSpaces() throws Exception {
    AssertUtils.assertXMLEquals( getClass().getResource( "AssertUtilsTest.2.xml" ), "<xml/>", true );
  }

  @Test
  public void testFormat() throws Exception {
    expectedException.expect( AssertionError.class );
    expectedException.expectMessage( "expected:<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<xml2 />> but was: <This is no xml!>" );

    AssertUtils.assertXMLEquals( "<xml2/>", "This is no xml!" );
  }

  @Test
  public void testAssertOne() {
    AssertUtils.assertOne( "a", "a", "b", "c" );
    AssertUtils.assertOne( "a", "b", "c", "a" );

    expectedException.expect( AssertionError.class );
    expectedException.expectMessage(
      "Expected: (is \"b\" or is \"c\")\n" +
        "     got: \"a\"" );
    AssertUtils.assertOne( "a", "b", "c" );
  }
}
