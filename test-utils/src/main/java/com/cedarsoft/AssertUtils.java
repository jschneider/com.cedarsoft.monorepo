package com.cedarsoft;

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

  public static void assertXMLEqual( Diff diff, boolean assertion ) {
    XMLAssert.assertXMLEqual( diff, assertion );
  }

  public static void assertXMLEqual( String msg, Diff diff, boolean assertion ) {
    XMLAssert.assertXMLEqual( msg, diff, assertion );
  }

  public static void assertXMLIdentical( Diff diff, boolean assertion ) {
    XMLAssert.assertXMLIdentical( diff, assertion );
  }

  public static void assertXMLIdentical( String msg, Diff diff, boolean assertion ) {
    XMLAssert.assertXMLIdentical( msg, diff, assertion );
  }

  public static void assertXMLEqual( InputSource test, InputSource control ) throws SAXException, IOException {
    XMLAssert.assertXMLEqual( test, control );
  }

  public static void assertXMLEqual( String test, String control ) throws SAXException, IOException {
    assertXMLEqual( test, control, false );
  }

  public static void assertXMLEqual( String test, String control, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    setIgnoreWhitespace( ignoreWhiteSpace );
    XMLAssert.assertXMLEqual( test, control );
    setIgnoreWhitespace( false );
  }

  public static void assertXMLEqual( String err, String test, String control, boolean ignoreWhiteSpace ) throws SAXException, IOException {
    setIgnoreWhitespace( ignoreWhiteSpace );
    XMLAssert.assertXMLEqual( err, test, control );
    setIgnoreWhitespace( false );
  }

  public static void assertXMLEqual( Document test, Document control ) {
    XMLAssert.assertXMLEqual( test, control );
  }

  public static void assertXMLEqual( Reader test, Reader control ) throws SAXException, IOException {
    XMLAssert.assertXMLEqual( test, control );
  }

  public static void assertXMLEqual( String err, InputSource test, InputSource control ) throws SAXException, IOException {
    XMLAssert.assertXMLEqual( err, test, control );
  }

  public static void assertXMLEqual( String err, String test, String control ) throws SAXException, IOException {
    XMLAssert.assertXMLEqual( err, test, control );
  }

  public static void assertXMLEqual( String err, Document test, Document control ) {
    XMLAssert.assertXMLEqual( err, test, control );
  }

  public static void assertXMLEqual( String err, Reader test, Reader control ) throws SAXException, IOException {
    XMLAssert.assertXMLEqual( err, test, control );
  }

  public static void assertXMLNotEqual( InputSource test, InputSource control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( test, control );
  }

  public static void assertXMLNotEqual( String test, String control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( test, control );
  }

  public static void assertXMLNotEqual( Document test, Document control ) {
    XMLAssert.assertXMLNotEqual( test, control );
  }

  public static void assertXMLNotEqual( Reader test, Reader control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( test, control );
  }

  public static void assertXMLNotEqual( String err, InputSource test, InputSource control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( err, test, control );
  }

  public static void assertXMLNotEqual( String err, String test, String control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( err, test, control );
  }

  public static void assertXMLNotEqual( String err, Document test, Document control ) {
    XMLAssert.assertXMLNotEqual( err, test, control );
  }

  public static void assertXMLNotEqual( String err, Reader test, Reader control ) throws SAXException, IOException {
    XMLAssert.assertXMLNotEqual( err, test, control );
  }

  public static void assertXMLValid( InputSource xml ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xml );
  }

  public static void assertXMLValid( String xmlString ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xmlString );
  }

  public static void assertXMLValid( String xmlString, String systemId ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xmlString, systemId );
  }

  public static void assertXMLValid( InputSource xml, String systemId ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xml, systemId );
  }

  public static void assertXMLValid( InputSource xml, String systemId, String doctype ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xml, systemId, doctype );
  }

  public static void assertXMLValid( String xmlString, String systemId, String doctype ) throws SAXException, ConfigurationException {
    XMLAssert.assertXMLValid( xmlString, systemId, doctype );
  }

  public static void assertXMLValid( Validator validator ) {
    XMLAssert.assertXMLValid( validator );
  }

  public static void assertNodeTestPasses( NodeTest control, NodeTester tester, short[] nodeTypes, boolean assertion ) {
    XMLAssert.assertNodeTestPasses( control, tester, nodeTypes, assertion );
  }

  public static void assertNodeTestPasses( String xmlString, NodeTester tester, short nodeType ) throws SAXException, IOException {
    XMLAssert.assertNodeTestPasses( xmlString, tester, nodeType );
  }

  public static void assertNodeTestPasses( InputSource xml, NodeTester tester, short nodeType ) throws SAXException, IOException {
    XMLAssert.assertNodeTestPasses( xml, tester, nodeType );
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
