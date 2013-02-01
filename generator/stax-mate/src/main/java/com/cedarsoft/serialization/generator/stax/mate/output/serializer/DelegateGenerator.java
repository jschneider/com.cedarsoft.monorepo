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
import com.cedarsoft.codegen.Expressions;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.FieldDeclarationInfo;
import com.cedarsoft.serialization.generator.common.output.serializer.AbstractGenerator;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;

import javax.annotation.Nonnull;

/**
 *
 */
public class DelegateGenerator extends AbstractDelegateGenerator {

  public static final String METHOD_NAME_DESERIALIZE = "deserialize";

  public static final String METHOD_NAME_SERIALIZE = "serialize";

  public DelegateGenerator( @Nonnull CodeGenerator codeGenerator ) {
    super( codeGenerator );
  }

  @Nonnull
  @Override
  public JStatement createAddToSerializeToExpression( @Nonnull AbstractGenerator<?> generator, @Nonnull JDefinedClass serializerClass, @Nonnull JExpression serializeTo, @Nonnull FieldDeclarationInfo fieldInfo, @Nonnull JVar object, JVar formatVersion ) {
    //Add serializer to constructor
    generator.addDelegatingSerializerToConstructor( serializerClass, codeGenerator.ref( TypeUtils.getErasure( fieldInfo.getType() ).toString() ) );

    JFieldVar constant = getConstant( serializerClass, fieldInfo );

    JExpression getterInvocation = codeGenerator.createGetterInvocation( object, fieldInfo );

    return JExpr.invoke( METHOD_NAME_SERIALIZE )
      .arg( getterInvocation )
      .arg( JExpr.dotclass( codeGenerator.ref( TypeUtils.getErasure( fieldInfo.getType() ).toString() ) ) )
      .arg( createAddElementExpression( serializeTo, constant ) )
      .arg( formatVersion )
      ;
  }

  @Nonnull
  @Override
  public Expressions createReadFromDeserializeFromExpression( @Nonnull AbstractGenerator<?> generator, @Nonnull JDefinedClass serializerClass, @Nonnull JExpression deserializeFrom, JVar wrapper, @Nonnull JVar formatVersion, @Nonnull FieldDeclarationInfo fieldInfo ) {
    JInvocation nextTagExpression = createNextTagInvocation( serializerClass, deserializeFrom, fieldInfo );

    JClass type = codeGenerator.ref( TypeUtils.getErasure( fieldInfo.getType() ).toString() );
    JInvocation expression = JExpr.invoke( METHOD_NAME_DESERIALIZE ).arg( JExpr.dotclass( type ) ).arg( formatVersion ).arg( deserializeFrom );
    return new Expressions( expression, nextTagExpression );
  }

  @Nonnull
  @Override
  public JClass generateFieldType( @Nonnull FieldDeclarationInfo fieldInfo ) {
    return codeGenerator.ref( TypeUtils.getErasure( fieldInfo.getType() ).toString() );
  }

  @Override
  public boolean canHandle( @Nonnull FieldDeclarationInfo fieldInfo ) {
    return true;
  }
}