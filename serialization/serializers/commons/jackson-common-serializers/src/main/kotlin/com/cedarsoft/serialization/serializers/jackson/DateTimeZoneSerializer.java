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
package it.neckar.open.serialization.serializers.jackson;

import it.neckar.open.serialization.jackson.AbstractJacksonSerializer;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionException;
import it.neckar.open.version.VersionRange;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.joda.time.DateTimeZone;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.IOException;

public class DateTimeZoneSerializer extends AbstractJacksonSerializer<DateTimeZone> {

  public static final String ID = "id";

  @Inject
  public DateTimeZoneSerializer() {
    super( "dateTimeZone", VersionRange.from( 1, 0, 0 ).to( 1, 0, 0 ) );
  }

  @Override
  public boolean isObjectType() {
    return false;
  }

  @Override
  public void serialize(@Nonnull JsonGenerator serializeTo, @Nonnull DateTimeZone objectToSerialize, @Nonnull Version formatVersion ) throws IOException, JsonProcessingException {
    verifyVersionWritable( formatVersion );

    serializeTo.writeString(objectToSerialize.getID() );
  }

  @Nonnull
  @Override
  public DateTimeZone deserialize( @Nonnull JsonParser deserializeFrom, @Nonnull Version formatVersion ) throws VersionException, IOException, JsonProcessingException {
    verifyVersionReadable( formatVersion );

    String id = deserializeFrom.getText();
    //Constructing the deserialized object
    return DateTimeZone.forID( id );
  }

}
