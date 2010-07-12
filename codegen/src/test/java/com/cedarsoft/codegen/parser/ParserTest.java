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

package com.cedarsoft.codegen.parser;

import com.sun.mirror.declaration.ClassDeclaration;
import org.junit.*;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class ParserTest {
  private File javaFile0;
  private File javaFile1;
  private File javaFile2;

  @Before
  public void setUp() throws Exception {
    {
      URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/FieldTypesInit.java" );
      assertNotNull( resource );
      javaFile0 = new File( resource.toURI() );
      assertTrue( javaFile0.exists() );
    }

    {
      URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Door.java" );
      assertNotNull( resource );
      javaFile1 = new File( resource.toURI() );
      assertTrue( javaFile1.exists() );
    }

    {
      URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Room.java" );
      assertNotNull( resource );
      javaFile2 = new File( resource.toURI() );
      assertTrue( javaFile2.exists() );
    }
  }

  @Test
  public void testSingle() {
    Result parsed = Parser.parse( javaFile0 );

    assertNotNull( parsed );
    assertEquals( 1, parsed.getClassDeclarations().size() );
    ClassDeclaration classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.FieldTypesInit" );

    assertNotNull( classDeclaration );
    assertThat( classDeclaration.getSimpleName(), is( "FieldTypesInit" ) );
    assertThat( classDeclaration.getQualifiedName(), is( "com.cedarsoft.codegen.model.test.FieldTypesInit" ) );
  }

  @Test
  public void testMulti() {
    Result parsed = Parser.parse( javaFile0, javaFile1, javaFile2 );
    assertNotNull( parsed );

    assertThat( parsed.getClassDeclarations().size(), is( 3 ) );
  }
}
