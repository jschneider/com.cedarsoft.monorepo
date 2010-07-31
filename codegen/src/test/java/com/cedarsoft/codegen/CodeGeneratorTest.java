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

package com.cedarsoft.codegen;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 *
 */
public class CodeGeneratorTest {
  private CodeGenerator<DecisionCallback> codeGenerator;

  @Before
  public void setUp() throws Exception {
    codeGenerator = new CodeGenerator<DecisionCallback>( new DecisionCallback() {
    } );
  }

  @Test
  public void testMethodDecorator() throws Exception {
    codeGenerator.addDecorator( new Decorator() {
      @Override
      public void decorateConstant( @NotNull CodeGenerator<?> codeGenerator, @NotNull JFieldVar constant ) {
        throw new UnsupportedOperationException();
      }
    } );
  }

  @Test
  public void testBasic() {
    assertNotNull( codeGenerator.getNewInstanceFactory() );
    assertNotNull( codeGenerator.getClassRefSupport() );
    assertNotNull( codeGenerator.getDecisionCallback() );
    assertNotNull( codeGenerator.getDecorators() );
    assertNotNull( codeGenerator.getModel() );
    assertNotNull( codeGenerator.getParseExpressionFactory() );
  }

  @Test
  public void testConstant() throws Exception {
    JCodeModel model = codeGenerator.getModel();

    codeGenerator.addDecorator( new Decorator() {
      @Override
      public void decorateConstant( @NotNull CodeGenerator<?> codeGenerator, @NotNull JFieldVar constant ) {
        constant.annotate( codeGenerator.ref( NotNull.class ) );
      }
    } );

    JDefinedClass aClass = model._class( "da.Class" );

    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );


    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.build( new SingleStreamCodeWriter( out ) );

    assertEquals( "-----------------------------------da.Class.java-----------------------------------\n" +
      "\n" +
      "package da;\n" +
      "\n" +
      "import org.jetbrains.annotations.NotNull;\n" +
      "\n" +
      "public class Class {\n" +
      "\n" +
      "    @NotNull\n" +
      "    public final static String DA_CONSTANT = \"value\";\n" +
      "\n" +
      "}", out.toString().trim() );
  }

  @Test
  public void testRef() {
    assertSame( codeGenerator.ref( "com.cedarsoft.Foo" ), codeGenerator.ref( "com.cedarsoft.Foo" ) );
    assertSame( codeGenerator.ref( String.class ), codeGenerator.ref( String.class ) );
    assertSame( codeGenerator.ref( FooBar.class ), codeGenerator.ref( FooBar.class ) );
    assertSame( codeGenerator.ref( FooBar.class ), codeGenerator.ref( "com.cedarsoft.codegen.CodeGeneratorTest$FooBar" ) );
  }

  private static class FooBar {

  }

}
