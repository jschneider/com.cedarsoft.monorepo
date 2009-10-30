package com.cedarsoft.serialization.stax;

import com.ctc.wstx.stax.WstxOutputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.codehaus.staxmate.out.SMOutputDocument;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class StaxTest {
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
    "</fileType>";

  @Test
  public void testStaxMate() throws XMLStreamException {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    SMOutputFactory smOutputFactory = new SMOutputFactory( factory );

    SMOutputDocument doc = smOutputFactory.createOutputDocument( out );
    doc.setIndentation( "\n  ", 1, 2 );

    SMOutputElement fileTypeElement = doc.addElement( "fileType" );
    fileTypeElement.addAttribute( "dependent", "false" );

    SMOutputElement idElement = fileTypeElement.addElement( "id" );
    idElement.addCharacters( "Canon Raw" );

    SMOutputElement extensionElement = fileTypeElement.addElement( "extension" );
    extensionElement.addAttribute( "default", "true" );
    extensionElement.addAttribute( "delimiter", "." );
    extensionElement.addCharacters( "cr2" );

    doc.closeRoot();

    assertEquals( out.toString(), CONTENT_SAMPLE );
  }

  @Test
  public void testBug() throws XMLStreamException {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    assertEquals( factory.getProperty( XMLOutputFactory.IS_REPAIRING_NAMESPACES ), false );
    factory.setProperty( XMLOutputFactory.IS_REPAIRING_NAMESPACES, false );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XMLStreamWriter writer = factory.createXMLStreamWriter( out );

    assertEquals( writer.getProperty( XMLOutputFactory.IS_REPAIRING_NAMESPACES ), false );
  }

  @Test
  public void testWrite() throws XMLStreamException {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XMLStreamWriter writer = factory.createXMLStreamWriter( out );

    writer.writeStartDocument();
    writer.writeStartElement( "fileType" );
    writer.writeAttribute( "dependent", "false" );

    writer.writeStartElement( "id" );
    writer.writeCharacters( "Canon Raw" );
    writer.writeEndElement();

    writer.writeStartElement( "extension" );
    writer.writeAttribute( "default", "true" );
    writer.writeAttribute( "delimiter", "." );
    writer.writeCharacters( "cr2" );
    writer.writeEndElement();

    writer.writeEndElement();
    writer.writeEndDocument();
    writer.close();

    assertEquals( out.toString(), CONTENT_SAMPLE );
  }

  @Test
  public void testStax() throws XMLStreamException {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    inputFactory.setProperty( XMLInputFactory.IS_COALESCING, true );
    XMLStreamReader parser = inputFactory.createXMLStreamReader( new StringReader( CONTENT_SAMPLE ) );

    assertEquals( parser.nextTag(), XMLStreamReader.START_ELEMENT );
    assertEquals( parser.getLocalName(), "fileType" );
    assertEquals( parser.getName().getLocalPart(), "fileType" );
    assertEquals( parser.getAttributeValue( null, "dependent" ), "false" );

    assertEquals( parser.nextTag(), XMLStreamReader.START_ELEMENT );
    assertEquals( parser.getName().getLocalPart(), "id" );
    assertEquals( parser.next(), XMLStreamReader.CHARACTERS );
    assertEquals( parser.getText(), "Canon Raw" );
    assertEquals( parser.nextTag(), XMLStreamReader.END_ELEMENT );
    assertEquals( parser.getName().getLocalPart(), "id" );

    assertEquals( parser.nextTag(), XMLStreamReader.START_ELEMENT );
    assertEquals( parser.getName().getLocalPart(), "extension" );
    assertEquals( parser.getAttributeValue( null, "default" ), "true" );
    assertEquals( parser.getAttributeValue( null, "delimiter" ), "." );
    assertEquals( parser.next(), XMLStreamReader.CHARACTERS );
    assertEquals( parser.getText(), "cr2" );
    assertEquals( parser.nextTag(), XMLStreamReader.END_ELEMENT );
    assertEquals( parser.getName().getLocalPart(), "extension" );

    assertEquals( parser.nextTag(), XMLStreamReader.END_ELEMENT );
    assertEquals( parser.getName().getLocalPart(), "fileType" );
    assertEquals( parser.next(), XMLStreamReader.END_DOCUMENT );
  }

  @Test
  public void testIterator() throws XMLStreamException {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    assertEquals( inputFactory.getProperty( XMLInputFactory.IS_COALESCING ), false );
    inputFactory.setProperty( XMLInputFactory.IS_COALESCING, true );
    assertEquals( inputFactory.getProperty( XMLInputFactory.IS_COALESCING ), true );
    //    inputFactory.setProperty(  );

    XMLEventReader parser = inputFactory.createXMLEventReader( new StringReader( CONTENT_SAMPLE ) );

    {
      XMLEvent event = parser.nextEvent();
      assertEquals( event.getEventType(), XMLEvent.START_DOCUMENT );
    }

    {
      XMLEvent event = parser.nextEvent();
      assertEquals( event.getEventType(), XMLEvent.START_ELEMENT );
      assertEquals( event.asStartElement().getName().getLocalPart(), "fileType" );
      assertEquals( event.asStartElement().getAttributeByName( new QName( null, "dependent" ) ).getValue(), "false" );
    }
  }
}
