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
package it.neckar.open.serialization.generator.intellij.stax.mate;

import it.neckar.open.serialization.generator.intellij.AbstractSerializerGenerator;
import it.neckar.open.serialization.generator.intellij.model.FieldToSerialize;
import it.neckar.open.serialization.generator.intellij.model.SerializerModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

import javax.annotation.Nonnull;

/**
 * A simple class that generates a jackson serializer
 *
 */
public class StaxMateSerializerGenerator extends AbstractSerializerGenerator {
  public StaxMateSerializerGenerator( @Nonnull Project project ) {
    super( project, "it.neckar.open.serialization.stax.mate.AbstractStaxMateSerializer", "org.codehaus.staxmate.out.SMOutputElement", "javax.xml.stream.XMLStreamReader", "javax.xml.stream.XMLStreamException" );
  }

  @Override
  protected void callSuperConstructor( @Nonnull SerializerModel serializerModel, @Nonnull StringBuilder constructorBuilder ) {
    constructorBuilder.append( "){" )
      .append( "super(\"" ).append( createType( serializerModel.getClassToSerializeQualifiedName() ) )
      .append( "\", \"http://cedarsoft.com/serialization/" ).append( serializerModel.getClassToSerializeQualifiedName() ).append( "\"" )
      .append( ", it.neckar.open.version.VersionRange.from(1,0,0).to());" );
  }

  @Override
  protected void appendDeserializeFieldStatements( @Nonnull SerializerModel serializerModel, @Nonnull StringBuilder methodBody ) {
    for ( FieldToSerialize field : serializerModel.getFieldToSerializeEntries() ) {

      //nextTag( deserializeFrom, ELEMENT );
      methodBody.append( "nextTag( deserializeFrom, " ).append( field.getPropertyConstantName() ).append( " );" );

      //Declare the field
      methodBody.append( field.getFieldType().getCanonicalText() ).append( " " ).append( field.getFieldName() ).append( "=" );

      //Deserialize
      methodBody.append( "deserialize(" )
        .append( field.getFieldTypeBoxed() ).append( ".class" )
        .append( ", formatVersion, deserializeFrom" )
        .append( ");" );
    }
  }
}
