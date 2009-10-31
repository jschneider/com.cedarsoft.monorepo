package com.cedarsoft;

import com.cedarsoft.utils.XmlCommons;
import junit.framework.AssertionFailedError;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.NodeTest;
import org.custommonkey.xmlunit.NodeTester;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static org.testng.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AssertUtils {
  private AssertUtils() {
  }

  public static void setIgnoreWhitespace( boolean ignore ) {
    XMLUnit.setIgnoreWhitespace( true );
  }

  public static void assertXMLEqual( String test, String control ) throws SAXException, IOException {
    assertXMLEqual( test, control, false );
  }

  public static void assertXMLEqual( String test, String control, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    assertXMLEqual( null, test, control, ignoreWhiteSpace );
  }

  public static void assertXMLEqual( String err, String test, String control, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    try {
      setIgnoreWhitespace( ignoreWhiteSpace );
      XMLAssert.assertXMLEqual( err, test, control );
      setIgnoreWhitespace( false );
    } catch ( AssertionFailedError e ) {
      throw new AssertionError( "expected:<" + XmlCommons.format( control ).trim() + "> but was:<" + XmlCommons.format( test ).trim() + '>' );
    }
  }

  public static void assertOne( @Nullable Object current, @NotNull Object... expectedAlternatives ) {
    List<AssertionError> failed = new ArrayList<AssertionError>();

    for ( Object expectedAlternative : expectedAlternatives ) {
      try {
        assertEquals( current, expectedAlternative );
        return; //Successfully
      } catch ( AssertionError e ) {
        failed.add( e );
      }
    }

    StringBuilder message = new StringBuilder();

    for ( AssertionError assertionError : failed ) {
      message.append( assertionError.getMessage() );
      message.append( "\n" );
    }

    throw new AssertionError( message.toString() );
  }
}
