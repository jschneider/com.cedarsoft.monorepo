package com.cedarsoft;

import org.custommonkey.xmlunit.XMLAssert;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 *
 */
public class XmlUnitTest {
  @Test
  public void testIt() throws IOException, SAXException {
    XMLAssert.assertXMLEqual( "<?xml version='1.0' encoding='UTF-8'?>\n" +
      "<fileType dependent=\"false\">\n" +
      "  <id>Canon Raw</id>\n" +
      "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
      "</fileType>",
                              "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<fileType dependent=\"false\">\n" +
                                "  <id>Canon Raw</id>\n" +
                                "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
                                "</fileType>"
    );
    AssertUtils.assertXMLEqual( "<?xml version='1.0' encoding='UTF-8'?>\n" +
      "<fileType dependent=\"false\">\n" +
      "  <id>Canon Raw</id>\n" +
      "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
      "</fileType>",
                                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                  "<fileType dependent=\"false\">\n" +
                                  "  <id>Canon Raw</id>\n" +
                                  "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
                                  "</fileType>"
    );
  }

  @Test
  public void testProblem() throws IOException, SAXException {
    AssertUtils.setIgnoreWhitespace( true );
    XMLAssert.assertXMLEqual( "<?xml version='1.0' encoding='UTF-8'?><fileType dependent=\"false\"><id>Canon Raw</id><extension default=\"true\" delimiter=\".\">cr2</extension></fileType>",
                              "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<fileType dependent=\"false\">\n" +
                                "  <id>Canon Raw</id>\n" +
                                "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
                                "</fileType>" );
    AssertUtils.assertXMLEqual( "<?xml version='1.0' encoding='UTF-8'?><fileType dependent=\"false\"><id>Canon Raw</id><extension default=\"true\" delimiter=\".\">cr2</extension></fileType>",
                                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                  "<fileType dependent=\"false\">\n" +
                                  "  <id>Canon Raw</id>\n" +
                                  "  <extension default=\"true\" delimiter=\".\">cr2</extension>\n" +
                                  "</fileType>" );
  }

}
