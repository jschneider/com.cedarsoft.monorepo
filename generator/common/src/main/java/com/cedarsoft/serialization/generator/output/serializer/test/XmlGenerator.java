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

package com.cedarsoft.serialization.generator.output.serializer.test;

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.serialization.test.utils.AbstractXmlSerializerTest2;
import com.cedarsoft.serialization.test.utils.AbstractXmlVersionTest2;
import com.cedarsoft.serialization.generator.decision.XmlDecisionCallback;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;

import javax.annotation.Nonnull;

/**
 *
 */
public class XmlGenerator extends AbstractGenerator<XmlDecisionCallback> {

  @Nonnull
  public static final String METHOD_GET_RESOURCE = "getResource";

  public XmlGenerator( @Nonnull CodeGenerator codeGenerator ) {
    super( codeGenerator );
  }

  @Nonnull
  @Override
  protected JClass createExtendsClass( @Nonnull JClass domainType, @Nonnull JClass serializerClass ) {
    return codeGenerator.ref( AbstractXmlSerializerTest2.class ).narrow( domainType );
  }

  @Nonnull
  @Override
  protected JClass createVersionExtendsClass( @Nonnull JClass domainType, @Nonnull JClass serializerClass ) {
    return codeGenerator.ref( AbstractXmlVersionTest2.class ).narrow( domainType );
  }

  @Nonnull
  @Override
  protected JExpression createExpectedExpression( @Nonnull JClass testClass, @Nonnull JClass domainType ) {
    String resourceName = domainType.name() + "_1.0.0_1.xml";

    JPackage testClassPackage = testClass._package();
    if ( !testClassPackage.hasResourceFile( resourceName ) ) {
      JTextFile resource = new JTextFile( resourceName );
      resource.setContents( createSampleContent( domainType ) );
      testClassPackage.addResourceFile( resource );
    }

    return testClass.dotclass().invoke( METHOD_GET_RESOURCE ).arg( resourceName );
  }

  private String createSampleContent( @Nonnull JClass domainType ) {
    String simpleName = NamingSupport.createVarName( domainType.name() );

    return "<?xml version=\"1.0\"?>\n" +
      "<" + simpleName + ">\n" +
      "</" + simpleName + ">\n"
      ;
  }
}
