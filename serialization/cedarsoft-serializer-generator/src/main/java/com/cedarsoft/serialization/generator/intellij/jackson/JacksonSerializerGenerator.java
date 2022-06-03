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
package com.cedarsoft.serialization.generator.intellij.jackson;

import com.cedarsoft.serialization.generator.intellij.AbstractSerializerGenerator;
import com.cedarsoft.serialization.generator.intellij.model.FieldToSerialize;
import com.cedarsoft.serialization.generator.intellij.model.SerializerModel;
import com.intellij.openapi.project.Project;

import javax.annotation.Nonnull;

/**
 * A simple class that generates a jackson serializer
 *
 */
public class JacksonSerializerGenerator extends AbstractSerializerGenerator {
  public JacksonSerializerGenerator( @Nonnull Project project ) {
    super( project, "com.cedarsoft.serialization.jackson.AbstractJacksonSerializer", "com.fasterxml.jackson.core.JsonGenerator", "com.fasterxml.jackson.core.JsonParser", "com.fasterxml.jackson.core.JsonProcessingException" );
  }

  @Override
  protected void callSuperConstructor( @Nonnull SerializerModel serializerModel, @Nonnull StringBuilder constructorBuilder ) {
    constructorBuilder.append( "){" )
      .append( "super(\"" ).append( createType( serializerModel.getClassToSerializeQualifiedName() ) ).append( "\", com.cedarsoft.version.VersionRange.from(1,0,0).to());" );
  }

  @Override
  protected void appendDeserializeFieldStatements( @Nonnull SerializerModel serializerModel, @Nonnull StringBuilder methodBody ) {
    //Declare the fields
    for ( FieldToSerialize field : serializerModel.getFieldToSerializeEntries() ) {
      if (field.isCollection()) {
        methodBody.append("java.util.List<? extends ").append(field.getFieldType().getCanonicalText()).append(">");
      }
      else {
        methodBody.append(field.getFieldType().getCanonicalText());
      }

      methodBody.append(" ").append(field.getFieldName()).append("=").append(field.getDefaultValue()).append(";");
    }

    methodBody.append( "\n\n" );

    {
      //While for fields
      methodBody.append( "com.cedarsoft.serialization.jackson.JacksonParserWrapper parser = new com.cedarsoft.serialization.jackson.JacksonParserWrapper( deserializeFrom );" +
                           "while ( parser.nextToken() == com.fasterxml.jackson.core.JsonToken.FIELD_NAME ) {" +
                           "String currentName = parser.getCurrentName();\n\n" );

      //add the ifs for the field names
      for ( FieldToSerialize field : serializerModel.getFieldToSerializeEntries() ) {
        String deserializeMethodName;
        if (field.isCollection()) {
          deserializeMethodName = "deserializeArray";
        }
        else {
          deserializeMethodName = "deserialize";
        }

        methodBody.append("if ( currentName.equals( ").append(field.getPropertyConstantName()).append(" ) ) {")
          .append( "parser.nextToken();" )

          .append(field.getFieldName()).append("=" + deserializeMethodName + "(")
          .append( field.getFieldTypeBoxed() ).append( ".class" )
          .append( ", formatVersion, deserializeFrom" )
          .append( ");" )

          .append( "continue;" )
          .append( "}" )
        ;
      }

      methodBody.append( "throw new IllegalStateException( \"Unexpected field reached <\" + currentName + \">\" );" );
      methodBody.append( "}" );
    }

    methodBody.append( "\n\n" );

    //Verify deserialization
    for ( FieldToSerialize field : serializerModel.getFieldToSerializeEntries() ) {
      if ( !field.shallVerifyDeserialized() ) {
        continue;
      }

      methodBody.append( "parser.verifyDeserialized(" ).append( field.getFieldName() ).append( "," ).append( field.getPropertyConstantName() ).append( ");" );
      if ( !field.isPrimitive() ) {
        methodBody.append( "assert " ).append( field.getFieldName() ).append( " !=" ).append( field.getDefaultValue() ).append( ";" );
      }
    }

    //clean up
    methodBody.append( "\n\n" );
    methodBody.append( "parser.ensureObjectClosed();" );
    methodBody.append( "\n\n" );
  }
}
