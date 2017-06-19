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

import java.io.IOException;
import java.util.BitSet;

import javax.annotation.Nonnull;

import com.cedarsoft.serialization.SerializationException;
import com.cedarsoft.serialization.jackson.AbstractJacksonSerializer;
import com.cedarsoft.serialization.jackson.JacksonParserWrapper;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class BitSetSerializer extends AbstractJacksonSerializer<BitSet> {
  public BitSetSerializer() {
    super("bitSet", VersionRange.single(1, 0, 0));
  }

  @Override
  public void serialize(@Nonnull JsonGenerator serializeTo, @Nonnull BitSet bitSet, @Nonnull Version formatVersion) throws IOException, VersionException, SerializationException, JsonProcessingException {
    serializeTo.writeStartArray();
    for (int i = bitSet.nextSetBit(0); i != -1; i = bitSet.nextSetBit(i + 1)) {
      serializeTo.writeNumber(i);
    }
    serializeTo.writeEndArray();
  }

  @Nonnull
  @Override
  public BitSet deserialize(@Nonnull JsonParser deserializeFrom, @Nonnull Version formatVersion) throws IOException, VersionException, SerializationException, JsonProcessingException {
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
