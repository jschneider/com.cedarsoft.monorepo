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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 */
public class XmlCommonsTest {
  private String lineSeparator;

  @BeforeEach
  public void setUp() throws Exception {
    lineSeparator = System.getProperty("line.separator");
  }

  @Test
  public void testFormat() {
    assertEquals( "", XmlCommons.format( "" ) );
    assertThat(XmlCommons.format("<xml><a/><b>asdf</b></xml>")).contains(lineSeparator);
  }

  @Test
  public void testOutDom() throws Exception {
    Document doc = XmlCommons.getDocumentBuilder().newDocument();

    Element root = doc.createElement( "root" );
    doc.appendChild( root );
    root.appendChild( doc.createElement( "child" ) );
    Element child = doc.createElement( "child" );
    root.appendChild( child );
    child.appendChild( doc.createElement( "another" ) );

    ByteArrayOutputStream out = new ByteArrayOutputStream();


    Transformer transformer = XmlCommons.createTransformer();

    transformer.transform( new DOMSource( doc ), new StreamResult( out ) );

    assertThat(out.toString()).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + lineSeparator +
                                           "<root>" + lineSeparator +
                                           "  <child/>" + lineSeparator +
                                           "  <child>" + lineSeparator +
                                           "    <another/>" + lineSeparator +
                                           "  </child>" + lineSeparator +
                                           "</root>" + lineSeparator
    );
  }

  @Test
  public void testOutStream() throws Exception {
    XMLOutputFactory factory = XMLOutputFactory.newFactory();

    Document doc = XmlCommons.getDocumentBuilder().newDocument();
    XMLStreamWriter writer = factory.createXMLStreamWriter( new DOMResult( doc ) );

    writer.writeStartElement( "daRoot" );
    writer.writeStartElement( "daChild" );
    writer.writeEndElement();
    writer.writeEndElement();
    writer.close();

    assertThat( doc.getXmlEncoding() ).isEqualTo( null );
    assertThat( doc.getXmlStandalone() ).isEqualTo( false );
    assertThat( doc.getXmlVersion() ).isEqualTo( "1.0" );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XmlCommons.out( doc, out );

    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + lineSeparator +
                   "<daRoot>" + lineSeparator +
                   "  <daChild/>" + lineSeparator +
                    "</daRoot>"
      , out.toString().trim() );
  }

  @Test
  public void testOutWriter() throws Exception {
    XMLOutputFactory factory = XMLOutputFactory.newFactory();

    Document doc = XmlCommons.getDocumentBuilder().newDocument();
    XMLStreamWriter writer = factory.createXMLStreamWriter( new DOMResult( doc ) );

    writer.writeStartElement( "daRoot" );
    writer.writeStartElement( "daChild" );
    writer.writeEndElement();
    writer.writeEndElement();

    StringWriter out = new StringWriter();
    XmlCommons.out( doc, out );

    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + lineSeparator +
                   "<daRoot>" + lineSeparator +
                   "  <daChild/>" + lineSeparator +
                    "</daRoot>"
      , out.toString().trim() );
  }

  @Test
  public void testParse() throws Exception {
    Document document = XmlCommons.parse( "<xml/>".getBytes(StandardCharsets.UTF_8) );
    assertEquals( "xml", document.getDocumentElement().getNodeName() );
  }
}
