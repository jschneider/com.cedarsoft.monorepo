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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SimpleSerializer extends com.cedarsoft.serialization.jackson.AbstractJacksonSerializer<Simple> {
    public static final String PROPERTY_FOO = "foo";

    @javax.inject.Inject
    public SimpleSerializer(@NotNull StringSerializer stringSerializer) {
        super("simple", com.cedarsoft.version.VersionRange.from(1, 0, 0).to());
        getDelegatesMappings().add(stringSerializer).responsibleFor(String.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        assert getDelegatesMappings().verify();
    }

    @Override
    public void serialize(@NotNull com.fasterxml.jackson.core.JsonGenerator serializeTo, @NotNull Simple object, @NotNull com.cedarsoft.version.Version formatVersion) throws IOException, com.cedarsoft.version.VersionException, com.fasterxml.jackson.core.JsonProcessingException {
        verifyVersionWritable(formatVersion);
        serialize(object.getFoo(), String.class, PROPERTY_FOO, serializeTo, formatVersion);
    }

    @Override
    @NotNull
    public Simple deserialize(@NotNull com.fasterxml.jackson.core.JsonParser deserializeFrom, @NotNull com.cedarsoft.version.Version formatVersion) throws IOException, com.cedarsoft.version.VersionException, com.fasterxml.jackson.core.JsonProcessingException {
        verifyVersionWritable(formatVersion);

        String foo = null;

        com.cedarsoft.serialization.jackson.JacksonParserWrapper parser = new com.cedarsoft.serialization.jackson.JacksonParserWrapper(deserializeFrom);
        while (parser.nextToken() == com.fasterxml.jackson.core.JsonToken.FIELD_NAME) {
            String currentName = parser.getCurrentName();

            if (currentName.equals(PROPERTY_FOO)) {
                parser.nextToken();
                foo = deserialize(String.class, formatVersion, deserializeFrom);
                continue;
            }
            throw new IllegalStateException("Unexpected field reached <" + currentName + ">");
        }

        parser.verifyDeserialized(foo, PROPERTY_FOO);
        assert foo != null;

        parser.ensureObjectClosed();

        Simple object = new Simple(foo);
        return object;
    }
}