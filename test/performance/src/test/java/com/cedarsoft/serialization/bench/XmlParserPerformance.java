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

package com.cedarsoft.serialization.bench;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.time.StopWatch;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.staxmate.SMInputFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.junit.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class XmlParserPerformance {
  public static final int SMALL = 5000;
  public static final int MEDIUM = SMALL * 10;
  public static final int BIG = MEDIUM * 10;

  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
    "</fileType>";
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE_JSON = "{\"fileType\":{\"@dependent\":\"false\",\"id\":\"Canon Raw\",\"extension\":{\"@default\":\"true\",\"@delimiter\":\".\",\"$\":\"cr2\"}}}";
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE_XSTREAM = "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">\n" +
    "    <extension>cr2</extension>\n" +
    "  </extension>\n" +
    "</fileType>";
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE_JAXB =
    "<fileType xmlns=\"http://test.cedarsoft.com/fileType\" dependent=\"false\">\n" +
      "  <id>Canon Raw</id>\n" +
      "  <extension default=\"true\" delimiter=\".\">\n" +
      "    <extension>cr2</extension>\n" +
      "  </extension>\n" +
      "</fileType>";
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE_JIBX =
    "<fileType xmlns=\"http://cedarsoft.com/serialization/bench/jaxb\" dependent=\"false\">\n" +
      " <id>Canon Raw</id>\n" +
      " <extension isDefault=\"true\">\n" +
      "  <delimiter>.</delimiter>\n" +
      "  <extension>cr2</extension>\n" +
      " </extension>\n" +
      "</fileType>";


  @Test
  public void testCreateXStream() {
    XStream xStream = createConfiguredXStream();

    FileType fileType = new FileType( "Canon Raw", new Extension( ".", "cr2", true ), false );
    assertEquals( CONTENT_SAMPLE_XSTREAM, xStream.toXML( fileType ) );
  }

  @NotNull
  private XStream createConfiguredXStream() {
    XStream xStream = new XStream();
    xStream.alias( "fileType", FileType.class );
    xStream.alias( "extension", Extension.class );
    xStream.useAttributeFor( FileType.class, "dependent" );

    xStream.useAttributeFor( Extension.class, "delimiter" );
    xStream.useAttributeFor( Extension.class, "isDefault" );
    xStream.aliasAttribute( Extension.class, "isDefault", "default" );
    return xStream;
  }

  //  /*
  //--> 640
  //--> 568
  //--> 597
  //--> 544
  //  */
  //  @Test
  //  public void testParseDomj4() throws DocumentException {
  //    runBenchmark( new Runnable() {
  //      public void run() {
  //        try {
  //          for ( int i = 0; i < 1000; i++ ) {
  //            new SAXReader().read( new StringReader( CONTENT_SAMPLE ) );
  //          }
  //        } catch ( DocumentException e ) {
  //          throw new RuntimeException( e );
  //        }
  //      }
  //    }, 4 );
  //  }


  public static void main( String[] args ) throws IOException {
    System.out.println();
    //Needs the plugin to be enabled (in pom.xml)
    //    System.out.println( "Jibx" );
    //    new XmlParserPerformance().benchJibx();
    System.out.println( "Json" );
    new XmlParserPerformance().benchJson();
    System.out.println( "Serialization (10%)" );
    new XmlParserPerformance().benchSerialization();
    System.out.println();
    System.out.println( "JAXB (10%)" );
    new XmlParserPerformance().benchJAXB();
    System.out.println();
    System.out.println( "Simple XML Serialization (10%)" );
    new XmlParserPerformance().benchSimpleXml();
    System.out.println();
    System.out.println( "Xstream (10%)" );
    new XmlParserPerformance().benchXStream();
    System.out.println();
    System.out.println( "Jdom (1% of documents):" );
    new XmlParserPerformance().benchJdom();
    System.out.println();
    System.out.println( "Java6:" );
    new XmlParserPerformance().benchJava();
    //        System.out.println();
    //        System.out.println( "Aalto:" );
    //        new XmlParserPerformance().benchAalto();
    System.out.println();
    System.out.println( "Woodstox:" );
    new XmlParserPerformance().benchWoodstox();
    System.out.println();
    System.out.println( "StaxMate + Woodstox:" );
    new XmlParserPerformance().benchStaxMateWoodstox();
    System.out.println();
    System.out.println( "Stax RI" );
    new XmlParserPerformance().benchStaxRI();
    System.out.println();
    System.out.println( "Javolution" );
    new XmlParserPerformance().benchJavolution();
  }

  public void benchSerialization() throws IOException {
    FileType type = new FileType( "Canon Raw", new Extension( ".", "cr2", true ), false );

    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream( bao );
    out.writeObject( type );
    out.close();

    final byte[] serialized = bao.toByteArray();

    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          for ( int i = 0; i < MEDIUM; i++ ) {
            assertNotNull( new ObjectInputStream( new ByteArrayInputStream( serialized ) ).readObject() );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchJibx() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          IBindingFactory bindingFactory = BindingDirectory.getFactory( com.cedarsoft.serialization.bench.jaxb.Extension.class );
          IUnmarshallingContext context = bindingFactory.createUnmarshallingContext();

          for ( int i = 0; i < BIG; i++ ) {
            assertNotNull( context.unmarshalDocument( new StringReader( CONTENT_SAMPLE_JIBX ) ) );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchJAXB() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          Unmarshaller unmarshaller = JAXBContext.newInstance( com.cedarsoft.serialization.bench.jaxb.FileType.class ).createUnmarshaller();

          for ( int i = 0; i < MEDIUM; i++ ) {
            assertNotNull( unmarshaller.unmarshal( new StringReader( CONTENT_SAMPLE_JAXB ) ) );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchXStream() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          XStream xStream = createConfiguredXStream();
          for ( int i = 0; i < MEDIUM; i++ ) {
            assertNotNull( xStream.fromXML( CONTENT_SAMPLE_XSTREAM ) );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchSimpleXml() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          Serializer serializer = new Persister();

          for ( int i = 0; i < MEDIUM; i++ ) {
            FileType read = serializer.read( FileType.class, new StringReader( CONTENT_SAMPLE_XSTREAM ) );
            assertNotNull( read );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchJdom() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          for ( int i = 0; i < SMALL; i++ ) {
            Document doc = new SAXBuilder().build( new StringReader( CONTENT_SAMPLE ) );

            Element fileTypeElement = doc.getRootElement();
            Element extensionElement = fileTypeElement.getChild( "extension" );

            Extension extension = new Extension( extensionElement.getAttributeValue( "delimiter" ), extensionElement.getText(), extensionElement.getAttribute( "default" ).getBooleanValue() );
            FileType fileType = new FileType( fileTypeElement.getChildText( "id" ), extension, fileTypeElement.getAttribute( "dependent" ).getBooleanValue() );

            assertNotNull( fileType );
          }
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  //  /*
  //--> 1766
  //--> 1786
  //--> 1775
  //--> 1783  */
  //  @Test
  //  public void testXppBench() throws DocumentException {
  //    runBenchmark( new Runnable() {
  //      public void run() {
  //        try {
  //          XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
  //          factory.setNamespaceAware( false );
  //
  //          for ( int i = 0; i < 100000; i++ ) {
  //            XmlPullParser parser = factory.newPullParser();
  //            assertEquals( parser.getClass(), MXParserCachingStrings.class );
  //
  //            parser.setInput( new StringReader( CONTENT_SAMPLE ) );
  //
  //            assertNull( parser.getName() );
  //            assertEquals( parser.nextTag(), XmlPullParser.START_TAG );
  //            assertEquals( parser.getName(), "fileType" );
  //            assertEquals( parser.getAttributeValue( null, "dependent" ), "false" );
  //
  //            assertEquals( parser.nextTag(), XmlPullParser.START_TAG );
  //            assertEquals( parser.getName(), "id" );
  //            assertEquals( parser.getText(), "<id>" );
  //            assertEquals( parser.nextText(), "Canon Raw" );
  //            assertEquals( parser.getText(), "</id>" );
  //
  //            assertEquals( parser.nextTag(), XmlPullParser.START_TAG );
  //            assertEquals( parser.getName(), "extension" );
  //            assertEquals( parser.getText(), "<extension default=\"true\" delimiter=\".\">" );
  //            assertEquals( parser.getAttributeValue( null, "default" ), "true" );
  //            assertEquals( parser.getAttributeValue( null, "delimiter" ), "." );
  //            assertEquals( parser.nextText(), "cr2" );
  //
  //            assertEquals( parser.nextTag(), XmlPullParser.END_TAG );
  //            assertEquals( parser.getText(), "</fileType>" );
  //            assertEquals( parser.next(), XmlPullParser.END_DOCUMENT );
  //          }
  //        } catch ( Exception e ) {
  //          throw new RuntimeException( e );
  //        }
  //      }
  //    }, 4 );
  //  }

  /*
--> 1705
--> 1701
--> 1764
--> 1693
   */

  public void benchJava() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          //          XMLInputFactory inputFactory = XMLInputFactory.newInstance( "StAXInputFactory", getClass().getClassLoader() );
          XMLInputFactory inputFactory = XMLInputFactory.newInstance( "com.sun.xml.internal.stream.XMLInputFactoryImpl", getClass().getClassLoader() );

          benchParse( inputFactory, CONTENT_SAMPLE );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  //  public void benchAalto() {
  //    runBenchmark( new Runnable() {
  //      @Override
  //      public void run() {
  //        try {
  //          //          XMLInputFactory inputFactory = XMLInputFactory.newInstance( "StAXInputFactory", getClass().getClassLoader() );
  //          XMLInputFactory inputFactory = XMLInputFactory.newInstance( "com.fasterxml.aalto.stax.InputFactoryImpl", getClass().getClassLoader() );
  //
  //          benchParse( inputFactory );
  //        } catch ( Exception e ) {
  //          throw new RuntimeException( e );
  //        }
  //      }
  //    }, 4 );
  //  }

  public void benchWoodstox() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          XMLInputFactory inputFactory = XMLInputFactory.newInstance( "com.ctc.wstx.stax.WstxInputFactory", getClass().getClassLoader() );

          benchParse( inputFactory, CONTENT_SAMPLE );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchJson() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          Configuration configuration = new Configuration();
          XMLInputFactory inputFactory = new MappedXMLInputFactory( configuration );

          benchParse( inputFactory, CONTENT_SAMPLE_JSON );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchStaxMateWoodstox() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          SMInputFactory inf = new SMInputFactory( XMLInputFactory.newInstance( "com.ctc.wstx.stax.WstxInputFactory", getClass().getClassLoader() ) );

          benchParse( inf.getStaxFactory(), CONTENT_SAMPLE );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchStaxRI() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          benchParse( XMLInputFactory.newInstance( "com.bea.xml.stream.MXParserFactory", getClass().getClassLoader() ), CONTENT_SAMPLE );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  public void benchJavolution() {
    runBenchmark( new Runnable() {
      @Override
      public void run() {
        try {
          javolution.xml.stream.XMLInputFactory inputFactory = javolution.xml.stream.XMLInputFactory.newInstance();
          benchParse( inputFactory );
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }

  private void benchParse( javolution.xml.stream.XMLInputFactory inputFactory ) throws XMLStreamException, javolution.xml.stream.XMLStreamException {
    for ( int i = 0; i < BIG; i++ ) {
      javolution.xml.stream.XMLStreamReader parser = inputFactory.createXMLStreamReader( new StringReader( CONTENT_SAMPLE ) );


      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "fileType", parser.getLocalName().toString() );

      boolean dependent = Boolean.parseBoolean( parser.getAttributeValue( null, "dependent" ).toString() );

      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "id", parser.getLocalName().toString() );
      assertEquals( XMLStreamReader.CHARACTERS, parser.next() );

      String id = parser.getText().toString();

      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );
      assertEquals( "id", parser.getLocalName().toString() );

      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "extension", parser.getLocalName().toString() );

      boolean isDefault = Boolean.parseBoolean( parser.getAttributeValue( null, "default" ).toString() );
      String delimiter = parser.getAttributeValue( null, "delimiter" ).toString();

      assertEquals( XMLStreamReader.CHARACTERS, parser.next() );

      String extension = parser.getText().toString();

      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );
      assertEquals( "extension", parser.getLocalName().toString() );

      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );
      assertEquals( "fileType", parser.getLocalName().toString() );
      assertEquals( XMLStreamReader.END_DOCUMENT, parser.next() );

      parser.close();

      FileType type = new FileType( id, new Extension( delimiter, extension, isDefault ), dependent );
      assertNotNull( type );
    }
  }

  private void benchParse( XMLInputFactory inputFactory, @NotNull @NonNls String contentSample ) throws XMLStreamException {
    for ( int i = 0; i < BIG; i++ ) {
      XMLStreamReader parser = inputFactory.createXMLStreamReader( new StringReader( contentSample ) );

      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "fileType", parser.getLocalName() );
      assertEquals( "fileType", parser.getName().getLocalPart() );

      boolean dependent = Boolean.parseBoolean( parser.getAttributeValue( null, "dependent" ) );

      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "id", parser.getName().getLocalPart() );
      assertEquals( XMLStreamReader.CHARACTERS, parser.next() );

      String id = parser.getText();

      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );

      assertEquals( XMLStreamReader.START_ELEMENT, parser.nextTag() );
      assertEquals( "extension", parser.getName().getLocalPart() );

      boolean isDefault = Boolean.parseBoolean( parser.getAttributeValue( null, "default" ) );
      String delimiter = parser.getAttributeValue( null, "delimiter" );

      assertEquals( XMLStreamReader.CHARACTERS, parser.next() );

      String extension = parser.getText();
      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );
      assertEquals( "extension", parser.getName().getLocalPart() );

      assertEquals( XMLStreamReader.END_ELEMENT, parser.nextTag() );
      assertEquals( "fileType", parser.getName().getLocalPart() );
      assertEquals( XMLStreamReader.END_DOCUMENT, parser.next() );

      parser.close();

      FileType type = new FileType( id, new Extension( delimiter, extension, isDefault ), dependent );
      assertNotNull( type );
    }
  }


  private void runBenchmark( @NotNull Runnable runnable, final int count ) {
    //Warmup
    runnable.run();
    runnable.run();
    runnable.run();

    List<Long> times = new ArrayList<Long>();

    for ( int i = 0; i < count; i++ ) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      runnable.run();
      stopWatch.stop();

      times.add( stopWatch.getTime() );
    }

    System.out.println( "-----------------------" );
    for ( Long time : times ) {
      System.out.println( "--> " + time );
    }
    System.out.println( "-----------------------" );
  }

  @Test
  public void testSimple() throws Exception {
    Serializer serializer = new Persister();
    FileType read = serializer.read( FileType.class, new StringReader( CONTENT_SAMPLE_XSTREAM ) );
    assertNotNull( read );
  }
}
