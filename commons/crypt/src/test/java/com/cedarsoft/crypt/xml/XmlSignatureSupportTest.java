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

package com.cedarsoft.crypt.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.cedarsoft.crypt.X509Support;

/**
 *
 */
public class XmlSignatureSupportTest {
  private X509Support x509Support;
  private XmlSignatureSupport signatureSupport;

  @Before
  public void setUp() throws Exception {
    x509Support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );
    signatureSupport = new XmlSignatureSupport( x509Support );
  }

  @Test
  public void testIt() {

  }

  @Test
  public void testSign() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.UNSIGNED.getBytes(StandardCharsets.UTF_8) ) );

    Document signedDocument = signatureSupport.sign( doc );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( signedDocument ), new StreamResult( out ) );

    assertEquals(XmlSignTest.SIGNED, new String(out.toByteArray(), StandardCharsets.UTF_8));
    assertEquals( XmlSignTest.SIGNED, out.toString() );
  }

  @Test
  public void testVerify() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes(StandardCharsets.UTF_8) ) );
    assertTrue( signatureSupport.hasValidSignature( doc ) );
  }

  @Test
  public void testGetDocument() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );

    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes(StandardCharsets.UTF_8) ) );

    Node originalNode = signatureSupport.getOriginalNode( doc );
    assertNotNull( originalNode );
    assertEquals( "invoice", originalNode.getNodeName() );

    Document original = signatureSupport.getOriginalDocument( doc );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( original ), new StreamResult( out ) );

    assertEquals(NEW_UNSINGED, new String(out.toByteArray(), StandardCharsets.UTF_8));
  }

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public static final String NEW_UNSINGED = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><invoice xmlns=\"http://www.company.com/accounting\">" + LINE_SEPARATOR +
    "  <items>" + LINE_SEPARATOR +
    "    <item>" + LINE_SEPARATOR +
    "      <desc>Applied Cryptography</desc>" + LINE_SEPARATOR +
    "      <type>book</type>" + LINE_SEPARATOR +
    "      <unitprice>44.50</unitprice>" + LINE_SEPARATOR +
    "      <quantity>1</quantity>" + LINE_SEPARATOR +
    "    </item>" + LINE_SEPARATOR +
    "  </items>" + LINE_SEPARATOR +
    "  <creditcard>" + LINE_SEPARATOR +
    "    <number>123456789</number>" + LINE_SEPARATOR +
    "    <expiry>10/20/2009</expiry>" + LINE_SEPARATOR +
    "    <lastname>John</lastname>" + LINE_SEPARATOR +
    "    <firstname>Smith</firstname>" + LINE_SEPARATOR +
    "  </creditcard>" + LINE_SEPARATOR +
    "</invoice>";
}
