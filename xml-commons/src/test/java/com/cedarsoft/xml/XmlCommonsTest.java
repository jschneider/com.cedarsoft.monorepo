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

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.*;
import org.junit.rules.*;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 *
 */
public class XmlCommonsTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testFormat() {
    assertEquals( "", XmlCommons.format( "" ) );
    assertEquals(
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml />\n\n"
      , XmlCommons.format( "<xml/>" ) );
  }


  @Test
  public void testOutput() throws IOException {
    File file = tmp.newFile( "out.xml" );

    Document doc = new Document();
    doc.setRootElement( new Element( "daRoot" ) );

    XmlCommons.writeXml( file, doc );

    assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<daRoot />"
      , XmlCommons.format( FileUtils.readFileToString( file ) ).trim() );
  }

  @Test
  public void testOut() throws Exception {
    XMLOutputFactory factory = XMLOutputFactory.newFactory();

    org.w3c.dom.Document doc = XmlCommons.getDocumentBuilder().newDocument();
    XMLStreamWriter writer = factory.createXMLStreamWriter( new DOMResult( doc ) );

    writer.writeStartElement( "daRoot" );
    writer.writeStartElement( "daChild" );
    writer.writeEndElement();
    writer.writeEndElement();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XmlCommons.out( doc, out );

    assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<daRoot>\n" +
      "  <daChild />\n" +
      "</daRoot>"
      , XmlCommons.format( out.toString() ).trim() );
  }

  @Test
  public void testOutWriter() throws Exception {
    XMLOutputFactory factory = XMLOutputFactory.newFactory();

    org.w3c.dom.Document doc = XmlCommons.getDocumentBuilder().newDocument();
    XMLStreamWriter writer = factory.createXMLStreamWriter( new DOMResult( doc ) );

    writer.writeStartElement( "daRoot" );
    writer.writeStartElement( "daChild" );
    writer.writeEndElement();
    writer.writeEndElement();

    StringWriter out = new StringWriter();
    XmlCommons.out( doc, out );

    assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<daRoot>\n" +
      "  <daChild />\n" +
      "</daRoot>"
      , XmlCommons.format( out.toString() ).trim() );
  }

  @Test
  public void testParse() throws IOException, SAXException {
    org.w3c.dom.Document document = XmlCommons.parse( "<xml/>".getBytes() );
    assertEquals( "xml", document.getDocumentElement().getNodeName() );
  }
}
