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

package com.cedarsoft.serialization.generator.stax.mate.output.serializer;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
public class StaxMateGeneratorTest extends AbstractGeneratorTest {
  protected StaxMateGenerator generator;

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    generator = new StaxMateGenerator( codeGenerator );
  }

  @Test
  public void testSerializer() throws IOException, JClassAlreadyExistsException {
    generator.generateSerializer( domainObjectDescriptor );

    JPackage thePackage = model._package( "com.cedarsoft.serialization.generator.stax.mate.test" );
    JDefinedClass definedClass = thePackage._getClass( "FooSerializer" );
    assertNotNull( definedClass );

    assertEquals( "FooSerializer", definedClass.name() );

    assertGeneratedCode( getClass().getResource( "StaxMateGeneratorTest.1.txt" ) );
  }
}
