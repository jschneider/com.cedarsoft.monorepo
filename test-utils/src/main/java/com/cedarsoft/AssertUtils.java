package com.cedarsoft;

import com.cedarsoft.xml.XmlCommons;
import junit.framework.AssertionFailedError;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

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
