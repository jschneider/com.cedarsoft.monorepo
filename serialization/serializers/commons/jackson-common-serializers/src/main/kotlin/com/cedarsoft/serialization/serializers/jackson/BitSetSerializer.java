/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package it.neckar.open.serialization.serializers.jackson;

import java.io.IOException;
import java.util.BitSet;

import javax.annotation.Nonnull;

import it.neckar.open.serialization.SerializationException;
import it.neckar.open.serialization.jackson.AbstractJacksonSerializer;
import it.neckar.open.serialization.jackson.JacksonParserWrapper;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionRange;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 *
 */
public class BitSetSerializer extends AbstractJacksonSerializer<BitSet> {
  public BitSetSerializer() {
    super("bitSet", VersionRange.single(1, 0, 0));
  }

  @Override
  public void serialize(@Nonnull JsonGenerator serializeTo, @Nonnull BitSet objectToSerialize, @Nonnull Version formatVersion) throws IOException, SerializationException {
    serializeTo.writeStartArray();
    for (int i = objectToSerialize.nextSetBit(0); i != -1; i = objectToSerialize.nextSetBit(i + 1)) {
      serializeTo.writeNumber(i);
    }
    serializeTo.writeEndArray();
  }

  @Nonnull
  @Override
  public BitSet deserialize(@Nonnull JsonParser deserializeFrom, @Nonnull Version formatVersion) throws IOException, SerializationException {
    BitSet bitSet = new BitSet();

    JacksonParserWrapper parserWrapper = new JacksonParserWrapper(deserializeFrom);
    while (parserWrapper.nextToken() != JsonToken.END_ARRAY) {
      Number value = parserWrapper.getNumberValue();
      bitSet.set(value.intValue());
    }

    return bitSet;
  }

  @Override
  public boolean isObjectType() {
    return false;
  }
}
