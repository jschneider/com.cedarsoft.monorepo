package com.cedarsoft.serialization.bench;

import org.apache.commons.lang.time.StopWatch;
import org.codehaus.staxmate.SMInputFactory;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 */
public class XmlParserPerformance {
  @NotNull
  @NonNls
  public static final String CONTENT_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<fileType dependent=\"false\">\n" +
    "  <id>Canon Raw</id>\n" +
    "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
    "</fileType>";

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


  public static void main( String[] args ) {
    System.out.println( "Jdom:" );
    new XmlParserPerformance().benchJdom();
    System.out.println();
    System.out.println( "Stax:" );
    new XmlParserPerformance().benchStax();
    System.out.println();
    System.out.println( "StaxMate:" );
    new XmlParserPerformance().benchStaxMate();
  }

  /*
--> 597
--> 586
--> 638
--> 500
  */
  public void benchJdom() {
    runBenchmark( new Runnable() {
      @java.lang.Override
      public void run() {
        try {
          for ( int i = 0; i < 1000; i++ ) {
            new SAXBuilder().build( new StringReader( CONTENT_SAMPLE ) );
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
  public void benchStax() {
    runBenchmark( new Runnable() {
      @java.lang.Override
      public void run() {
        try {
          XMLInputFactory inputFactory = XMLInputFactory.newInstance();

          for ( int i = 0; i < 100000; i++ ) {
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
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
  }
  
  public void benchStaxMate() {
    runBenchmark( new Runnable() {
      @java.lang.Override
      public void run() {
        try {
          SMInputFactory inf = new SMInputFactory( XMLInputFactory.newInstance() );


          for ( int i = 0; i < 100000; i++ ) {
            XMLStreamReader parser = inf.createStax2Reader( new StringReader( CONTENT_SAMPLE ) );

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
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }, 4 );
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


}
