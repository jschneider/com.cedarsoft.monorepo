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

public class SetterSerializer extends it.neckar.open.serialization.stax.mate.AbstractStaxMateSerializer<Setter> {
    public static final String PROPERTY_FOO = "foo";

    @javax.inject.Inject
    public SetterSerializer(@NotNull StringSerializer stringSerializer) {
        super("setter", "http://cedarsoft.com/serialization/Setter", it.neckar.open.version.VersionRange.from(1, 0, 0).to());
        getDelegatesMappings().add(stringSerializer).responsibleFor(String.class).map(1, 0, 0).toDelegateVersion(1, 0, 0);
        assert getDelegatesMappings().verify();
    }

    @Override
    public void serialize(@NotNull org.codehaus.staxmate.out.SMOutputElement serializeTo, @NotNull Setter object, @NotNull it.neckar.open.version.Version formatVersion) throws IOException, it.neckar.open.version.VersionException, javax.xml.stream.XMLStreamException {
        verifyVersionWritable(formatVersion);
        serialize(object.getFoo(), String.class, PROPERTY_FOO, serializeTo, formatVersion);
    }

    @Override
    @NotNull
    public Setter deserialize(@NotNull javax.xml.stream.XMLStreamReader deserializeFrom, @NotNull it.neckar.open.version.Version formatVersion) throws IOException, it.neckar.open.version.VersionException, javax.xml.stream.XMLStreamException {
        verifyVersionWritable(formatVersion);

        nextTag(deserializeFrom, PROPERTY_FOO);
        String foo = deserialize(String.class, formatVersion, deserializeFrom);
        Setter object = new Setter();
        object.setFoo(foo);
        return object;
    }
}
