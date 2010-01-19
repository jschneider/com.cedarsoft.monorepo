/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Common xml methods
 */
public class XmlCommons {
  private XmlCommons() {
  }

  @NotNull
  @NonNls
  public static String format( @NotNull @NonNls String xml ) {
    if ( xml.length() == 0 ) {
      return "";
    }

    try {
      Document doc = new SAXBuilder().build( new StringReader( xml ) );
      Format format = Format.getPrettyFormat();
      format.setLineSeparator( "\n" );
      return new XMLOutputter( format ).outputString( doc );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * Write the document to the given file
   *
   * @param file     the file
   * @param document the document
   * @throws IOException if an io exception occures
   */
  public static void writeXml( @NotNull File file, @NotNull Document document ) throws IOException {
    Writer writer = null;
    try {
      writer = new BufferedWriter( new FileWriter( file ) );
      new XMLOutputter( Format.getPrettyFormat() ).output( document, writer );
    } finally {
      if ( writer != null ) {
        writer.close();
      }
    }
  }

  @NotNull
  public static DocumentBuilder getDocumentBuilder() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware( true );
      return factory.newDocumentBuilder();
    } catch ( ParserConfigurationException e ) {
      throw new RuntimeException( e );
    }
  }

  public static void out( @NotNull org.w3c.dom.Document document, @NotNull OutputStream out ) {
    try {
      TransformerFactory.newInstance().newTransformer().transform( new DOMSource( document ), new StreamResult( out ) );
    } catch ( TransformerException e ) {
      throw new RuntimeException( e );
    }
  }

  public static void out( @NotNull org.w3c.dom.Document document, @NotNull Writer out ) {
    try {
      TransformerFactory.newInstance().newTransformer().transform( new DOMSource( document ), new StreamResult( out ) );
    } catch ( TransformerException e ) {
      throw new RuntimeException( e );
    }
  }

  @NotNull
  public static Document toJDom( @NotNull org.w3c.dom.Document document ) {
    try {
      JDOMResult target = new JDOMResult();
      TransformerFactory.newInstance().newTransformer().transform( new DOMSource( document ), target );
      return target.getDocument();
    } catch ( TransformerException e ) {
      throw new RuntimeException( e );
    }
  }

  @NotNull
  public static org.w3c.dom.Document parse( @NotNull byte[] bytes ) throws IOException, SAXException {
    return parse( new ByteArrayInputStream( bytes ) );
  }

  @NotNull
  public static org.w3c.dom.Document parse( @NotNull InputStream in ) throws IOException, SAXException {
    return getDocumentBuilder().parse( in );
  }

  @NotNull
  public static String toString( org.w3c.dom.Document document ) {
    StringWriter stringWriter = new StringWriter();
    out( document, stringWriter );
    return stringWriter.toString();
  }
}
