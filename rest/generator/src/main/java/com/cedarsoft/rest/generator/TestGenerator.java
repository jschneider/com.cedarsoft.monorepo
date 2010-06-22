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

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.DecisionCallback;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.rest.AbstractJaxbTest;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type of the decision callback
 */
public class TestGenerator<T extends DecisionCallback> extends AbstractGenerator<T> {
  public TestGenerator( @NotNull CodeGenerator<T> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generateTest() throws JClassAlreadyExistsException {
    JClass jaxbClass = codeGenerator.ref( getJaxbClassName() );
    JDefinedClass testClass = codeGenerator.getModel()._class( getTestClassName() )._extends( codeGenerator.ref( AbstractJaxbTest.class ) );

    {
      JMethod method = testClass.method( JMod.PROTECTED, codeGenerator.ref( Class.class ).narrow( jaxbClass ), "getJaxbType" );
      method.annotate( Override.class );
      method.body()._return( jaxbClass.dotclass() );
    }

    {
      JMethod method = testClass.method( JMod.PROTECTED, String.class, "expectedXml" );
      method.annotate( Override.class );
      method.body()._return( JExpr.lit( "<todo/>" ) );
    }

    {
      JMethod method = testClass.method( JMod.PROTECTED, jaxbClass, "createObjectToSerialize" )._throws( Exception.class );
      method.annotate( Override.class );
      JVar field = method.body().decl( jaxbClass, "object", JExpr._new( jaxbClass ) );

      //Sets the values
      for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldsToSerialize() ) {
        JExpression value = codeGenerator.getNewInstanceFactory().create( fieldInfo.getType(), fieldInfo.getSimpleName() );
        method.body().add( field.invoke( NamingSupport.createSetter( fieldInfo.getSimpleName() ) ).arg( value ) );
      }

      method.body()._return( field );
    }
  }

  private String getTestClassName() {
    return getJaxbClassName() + "Test";
  }
}
//  @NotNull
//  @Override
//  protected Foo createObjectToSerialize() throws Exception {
//    Foo foo = new Foo();
//    foo.setDaValue( "daValueA" );
//    foo.setHref( new URI( "my:uri" ) );
//    foo.setId( "daId" );
//    return foo;
//  }
