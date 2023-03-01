/**
 * Copyright (C) cedarsoft GmbH.
 *
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package it.neckar.open.serialization.serializers.jackson

import it.neckar.open.app.ApplicationInformation
import it.neckar.open.serialization.jackson.AbstractJacksonSerializer
import it.neckar.open.serialization.jackson.JacksonParserWrapper
import it.neckar.open.version.Version
import it.neckar.open.version.VersionRange.Companion.from
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import java.io.IOException
import javax.annotation.Nonnull
import javax.inject.Inject

class ApplicationSerializer @Inject constructor(@Nonnull versionSerializer: VersionSerializer) :
  AbstractJacksonSerializer<ApplicationInformation>(
    "application",
    from(1, 0, 0).to(1, 0, 0)
  ) {
  /**
   * @param versionSerializer the version serializer
   * @noinspection TypeMayBeWeakened
   */
  init {
    add(versionSerializer).responsibleFor(Version::class.java).map(1, 0, 0).toDelegateVersion(1, 0, 0)
    assert(delegatesMappings.verify())
  }

  @Throws(IOException::class, JsonProcessingException::class)
  override fun serialize(@Nonnull serializeTo: JsonGenerator, @Nonnull objectToSerialize: ApplicationInformation, @Nonnull formatVersion: Version) {
    verifyVersionReadable(formatVersion)
    //name
    serializeTo.writeStringField(PROPERTY_NAME, objectToSerialize.name)
    //version
    serialize(objectToSerialize.version, Version::class.java, PROPERTY_VERSION, serializeTo, formatVersion)
  }

  @Nonnull
  @Throws(IOException::class)
  override fun deserialize(@Nonnull deserializeFrom: JsonParser, @Nonnull formatVersion: Version): ApplicationInformation {
    //name
    val parserWrapper = JacksonParserWrapper(deserializeFrom)
    parserWrapper.nextToken()
    parserWrapper.verifyCurrentToken(JsonToken.FIELD_NAME)
    val currentName = parserWrapper.currentName
    if (PROPERTY_NAME != currentName) {
      throw JsonParseException(parserWrapper.parser, "Invalid field. Expected <" + PROPERTY_NAME + "> but was <" + currentName + ">", parserWrapper.currentLocation)
    }
    parserWrapper.nextToken()
    val name = parserWrapper.text
    //version
    val version = deserialize(Version::class.java, PROPERTY_VERSION, formatVersion, deserializeFrom)
    //Finally closing element
    parserWrapper.nextToken(JsonToken.END_OBJECT)
    //Constructing the deserialized object
    return ApplicationInformation(name, version)
  }

  companion object {
    const val PROPERTY_NAME: String = "name"
    const val PROPERTY_VERSION: String = "version"
  }
}
