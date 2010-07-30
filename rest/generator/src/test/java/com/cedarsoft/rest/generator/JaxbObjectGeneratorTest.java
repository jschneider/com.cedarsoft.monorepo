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

package com.cedarsoft.rest.generator;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;


/**
 *
 */
public class JaxbObjectGeneratorTest {
  private DomainObjectDescriptor descriptor;
  private CodeGenerator<JaxbObjectGenerator.MyDecisionCallback> codeGenerator;

  @Before
  public void setUp() throws Exception {
    Result result = Parser.parse( new File( getClass().getResource( "test/BarModel.java" ).toURI() ) );
    descriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration() ).create();
    codeGenerator = new CodeGenerator<JaxbObjectGenerator.MyDecisionCallback>( new JaxbObjectGenerator.MyDecisionCallback() );
  }

  @Test
  public void testGeneratModelBar() throws URISyntaxException, JClassAlreadyExistsException, IOException {
    new Generator( codeGenerator, descriptor ).generate();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( getClass().getResource( "JaxbObjectGeneratorTest.Bar.txt" ), out.toString() );
  }

  @Test
  public void testGeneratModelFoo() throws URISyntaxException, JClassAlreadyExistsException, IOException {
    Result result = Parser.parse( new File( getClass().getResource( "test/FooModel.java" ).toURI() ) );
    descriptor = new DomainObjectDescriptorFactory( result.getClassDeclaration() ).create();
    codeGenerator = new CodeGenerator<JaxbObjectGenerator.MyDecisionCallback>( new JaxbObjectGenerator.MyDecisionCallback() );


    new Generator( codeGenerator, descriptor ).generate();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( getClass().getResource( "JaxbObjectGeneratorTest.Foo.txt" ), out.toString() );
  }

  @Test
  public void testGeneratTest() throws Exception {
    new TestGenerator( codeGenerator, descriptor ).generateTest();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeGenerator.getModel().build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals( getClass().getResource( "JaxbObjectGeneratorTest.test.txt" ), out.toString() );
  }

  @Test
  public void testPac() {
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "ins" ), "a.b.c.d.ins.E" );
    assertEquals( Generator.insertSubPackage( "a.b.c.d.E", "e" ), "a.b.c.d.e.E" );
  }
}
