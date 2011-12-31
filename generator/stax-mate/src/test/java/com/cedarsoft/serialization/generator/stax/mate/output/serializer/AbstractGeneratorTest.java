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

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.cedarsoft.serialization.generator.common.decision.DefaultXmlDecisionCallback;
import com.cedarsoft.serialization.generator.common.output.serializer.NotNullDecorator;
import com.cedarsoft.test.utils.AssertUtils;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.junit.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 *
 */
public abstract class AbstractGeneratorTest {
  protected DomainObjectDescriptor domainObjectDescriptor;
  protected CodeGenerator codeGenerator;

  protected JCodeModel model;

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getResource( "/com/cedarsoft/serialization/generator/stax/mate/test/Foo.java" );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );
    Result parsed = Parser.parse( null, javaFile );
    assertNotNull( parsed );

    TypeUtils.setTypes( parsed.getEnvironment().getTypeUtils() );
    DomainObjectDescriptorFactory factory = new DomainObjectDescriptorFactory( parsed.getClassDeclaration( "com.cedarsoft.serialization.generator.stax.mate.test.Foo" ) );
    domainObjectDescriptor = factory.create();
    assertNotNull( domainObjectDescriptor );

    assertEquals( 7, domainObjectDescriptor.getFieldInfos().size() );
    final DefaultXmlDecisionCallback decisionCallback = new DefaultXmlDecisionCallback( "width", "height" );
    CodeGenerator codeGenerator = new CodeGenerator( decisionCallback );
    this.codeGenerator = codeGenerator;
    this.codeGenerator.addDecorator( new NotNullDecorator( Nonnull.class ) );
    model = codeGenerator.getModel();
  }

  protected void assertGeneratedCode( @Nonnull URL expected ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.build( new SingleStreamCodeWriter( out ) );

    AssertUtils.assertEquals(expected, out.toString().trim());
  }

  protected void assertGeneratedCode( @Nonnull String expected ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.build( new SingleStreamCodeWriter( out ) );

    assertEquals( out.toString().trim(), expected.trim() );
  }
}