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

package it.neckar.open.serialization.test.performance;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.jupiter.api.*;
import org.xml.sax.SAXException;

import it.neckar.open.serialization.test.performance.jaxb.Extension;
import it.neckar.open.serialization.test.performance.jaxb.FileType;
import it.neckar.open.test.utils.AssertUtils;

/**
 *
 */
public class JibxTest {
  @Disabled
  @Test
  public void testIt() throws JiBXException, IOException, SAXException {
    IBindingFactory bindingFactory = BindingDirectory.getFactory( Extension.class );
    assertNotNull( bindingFactory );

    IMarshallingContext context = bindingFactory.createMarshallingContext();


    it.neckar.open.serialization.test.performance.jaxb.FileType type = new FileType( "jpg", new it.neckar.open.serialization.test.performance.jaxb.Extension( ".", "jpg", true ), false );

    StringWriter out = new StringWriter();
    context.marshalDocument( type, "UTF-8", null, out );

    AssertUtils.assertXMLEquals(out.toString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<fileType xmlns=\"http://cedarsoft.com/serialization/bench/jaxb\" dependent=\"false\">\n" +
      " <id>jpg</id>\n" +
      " <extension isDefault=\"true\">\n" +
      "  <delimiter>.</delimiter>\n" +
      "  <extension>jpg</extension>\n" +
      " </extension>\n" +
      "</fileType>");
  }
}
