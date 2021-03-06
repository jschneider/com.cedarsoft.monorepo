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

package com.cedarsoft.serialization.serializers.jackson;

import com.cedarsoft.app.ApplicationInformation;
import com.cedarsoft.serialization.jackson.AbstractJacksonSerializer;
import com.cedarsoft.serialization.jackson.JacksonParserWrapper;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

public class ApplicationSerializer extends AbstractJacksonSerializer<ApplicationInformation> {

  public static final String PROPERTY_NAME = "name";

  public static final String PROPERTY_VERSION = "version";

  /**
   * @param versionSerializer the version serializer
   * @noinspection TypeMayBeWeakened
   */
  @Inject
  public ApplicationSerializer( @Nonnull VersionSerializer versionSerializer ) {
    super( "application", VersionRange.from( 1, 0, 0 ).to( 1, 0, 0 ) );
    add( versionSerializer ).responsibleFor( Version.class ).map( 1, 0, 0 ).toDelegateVersion( 1, 0, 0 );
    assert getDelegatesMappings().verify();
  }

  @Override
  public void serialize(@Nonnull JsonGenerator serializeTo, @Nonnull ApplicationInformation objectToSerialize, @Nonnull Version formatVersion ) throws IOException, JsonProcessingException {
    verifyVersionReadable( formatVersion );
    //name
    serializeTo.writeStringField(PROPERTY_NAME, objectToSerialize.getName() );
    //version
    serialize(objectToSerialize.getVersion(), Version.class, PROPERTY_VERSION, serializeTo, formatVersion );
  }

  @Nonnull
  @Override
  public ApplicationInformation deserialize(@Nonnull JsonParser deserializeFrom, @Nonnull Version formatVersion ) throws VersionException, IOException, JsonProcessingException {
    //name
    JacksonParserWrapper parserWrapper = new JacksonParserWrapper( deserializeFrom );
    parserWrapper.nextToken();
    parserWrapper.verifyCurrentToken( JsonToken.FIELD_NAME );
    String currentName = parserWrapper.getCurrentName();

    if ( !PROPERTY_NAME.equals( currentName ) ) {
      throw new JsonParseException(parserWrapper.getParser(), "Invalid field. Expected <" + PROPERTY_NAME + "> but was <" + currentName + ">", parserWrapper.getCurrentLocation());
    }
    parserWrapper.nextToken();
    String name = parserWrapper.getText();
    //version
    Version version = deserialize( Version.class, PROPERTY_VERSION, formatVersion, deserializeFrom );
    //Finally closing element
    parserWrapper.nextToken( JsonToken.END_OBJECT );
    //Constructing the deserialized object
    return new ApplicationInformation(name, version );
  }

}
