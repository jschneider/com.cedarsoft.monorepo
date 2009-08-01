package com.cedarsoft.utils;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
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
import java.io.StringWriter;
import java.io.Writer;

/**
 * Common xml methods
 */
public class XmlCommons {
  private XmlCommons() {
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
