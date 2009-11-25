package com.cedarsoft.crypt.xml;

import com.cedarsoft.crypt.X509Support;
import org.testng.annotations.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.testng.Assert.*;

/**
 *
 */
public class XmlSignatureSupportTest {
  private X509Support x509Support;
  private XmlSignatureSupport signatureSupport;

  @BeforeMethod
  protected void setUp() throws Exception {
    x509Support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );
    signatureSupport = new XmlSignatureSupport( x509Support );
  }

  @Test
  public void testIt() {

  }

  @Test
  public void testSign() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.UNSIGNED.getBytes() ) );

    Document signedDocument = signatureSupport.sign( doc );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( signedDocument ), new StreamResult( out ) );

    assertEquals( XmlSignTest.SIGNED, new String( out.toByteArray() ) );
    assertEquals( XmlSignTest.SIGNED, out.toString() );
  }

  @Test
  public void testVerify() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes() ) );
    assertTrue( signatureSupport.hasValidSignature( doc ) );
  }

  @Test
  public void testGetDocument() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );

    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes() ) );

    Node originalNode = signatureSupport.getOriginalNode( doc );
    assertNotNull( originalNode );
    assertEquals( "invoice", originalNode.getNodeName() );

    Document original = signatureSupport.getOriginalDocument( doc );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( original ), new StreamResult( out ) );

    assertEquals( NEW_UNSINGED, new String( out.toByteArray() ) );
  }

  public static final String NEW_UNSINGED = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><invoice xmlns=\"http://www.company.com/accounting\">\n" +
    "  <items>\n" +
    "    <item>\n" +
    "      <desc>Applied Cryptography</desc>\n" +
    "      <type>book</type>\n" +
    "      <unitprice>44.50</unitprice>\n" +
    "      <quantity>1</quantity>\n" +
    "    </item>\n" +
    "  </items>\n" +
    "  <creditcard>\n" +
    "    <number>123456789</number>\n" +
    "    <expiry>10/20/2009</expiry>\n" +
    "    <lastname>John</lastname>\n" +
    "    <firstname>Smith</firstname>\n" +
    "  </creditcard>\n" +
    "</invoice>";
}
