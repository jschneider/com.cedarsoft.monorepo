package com.cedarsoft;

import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.testng.Assert.*;

/**
 *
 */
public class AssertUtilsTest {
  @Test
  public void testXml() throws IOException, SAXException {
    try {
      AssertUtils.assertXMLEqual( "<xml/>", "<xml2/>" );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
      System.out.println( "---------" );
      System.out.println( e.getMessage() );
      System.out.println( "---------" );
      assertEquals( e.getMessage().trim(), ( "expected:<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml2 />> but was:<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<xml />>" ).trim() );
    }
  }
}
