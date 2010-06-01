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

package com.cedarsoft.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.testng.annotations.*;

/**
 */
public class XpathTest {
  private Element root;
  private Document doc;

  @BeforeMethod
  protected void setUp() throws Exception {
    root = new Element( "root" );
    doc = new Document( root );

    root.addContent( new Element( "child" ).addContent( new Element( "a" ).setText( "_0" ) ) );
    root.addContent( new Element( "child" ).addContent( new Element( "b" ).setText( "_1" ) ) );
    root.addContent( new Element( "child" ).setText( "_2" ) );
    root.addContent( new Element( "child" ).setText( "_3" ) );
  }

  @Test
  public void testDummy() {

  }

  //  public void testIt() throws JDOMException {
  //    XPath path = XPath.newInstance( "/*" );
  //    assertEquals( "/*", path.getXPath() );
  //    Element queried = ( Element ) path.selectSingleNode( doc );
  //    assertSame( root, queried );
  //  }
  //
  //  public void testMore() throws JDOMException {
  //    Element queried = ( Element ) XPath.newInstance( "/root/child[1]/a" ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( "a", queried.getName() );
  //    assertEquals( "_0", queried.getText() );
  //    assertEquals( 0, queried.getChildren().size() );
  //  }
  //
  //  public void testMore2() throws JDOMException {
  //    Element queried = ( Element ) XPath.newInstance( "/root/child[2]/b[1]" ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( "b", queried.getName() );
  //    assertEquals( "_1", queried.getText() );
  //    assertEquals( 0, queried.getChildren().size() );
  //  }
  //
  //  public void testPath() throws JDOMException {
  //    String xpath = "/root/child[2]/b";
  //    Element queried = ( Element ) XPath.newInstance( xpath ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( xpath, XPathCreator.createAbsolutePath( queried ) );
  //  }
}

