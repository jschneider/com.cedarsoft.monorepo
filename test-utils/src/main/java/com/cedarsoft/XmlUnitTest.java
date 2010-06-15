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

package com.cedarsoft;

import org.custommonkey.xmlunit.XMLAssert;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * <p>XmlUnitTest class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XmlUnitTest {
  /**
   * <p>testIt</p>
   *
   * @throws java.io.IOException if any.
   * @throws org.xml.sax.SAXException if any.
   */
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

  /**
   * <p>testProblem</p>
   *
   * @throws java.io.IOException if any.
   * @throws org.xml.sax.SAXException if any.
   */
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
