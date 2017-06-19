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

public class PrimitivesSerializerVersionTest extends com.cedarsoft.serialization.test.utils.AbstractJsonVersionTest2<Primitives> {
    @NotNull
    @org.junit.experimental.theories.DataPoint
    public static final com.cedarsoft.serialization.test.utils.VersionEntry ENTRY1 = com.cedarsoft.serialization.test.utils.AbstractJsonVersionTest2.create(
            com.cedarsoft.version.Version.valueOf(1, 0, 0), PrimitivesSerializerVersionTest.class.getResource("Primitives_1.0.0_1.json"));

    @NotNull
    @Override
    protected com.cedarsoft.serialization.StreamSerializer<Primitives> getSerializer() throws Exception {
        return com.google.inject.Guice.createInjector().getInstance(PrimitivesSerializer.class);
    }

    @Override
    protected void verifyDeserialized(@NotNull Primitives deserialized, @NotNull com.cedarsoft.version.Version version) {
        org.junit.Assert.assertNotNull(deserialized.getFoo1());
        org.junit.Assert.assertNotNull(deserialized.getFoo2());
        org.junit.Assert.assertNotNull(deserialized.getFoo3());
        org.junit.Assert.assertNotNull(deserialized.getFoo4());
        org.junit.Assert.assertNotNull(deserialized.getFoo5());
        org.junit.Assert.assertNotNull(deserialized.getFoo6());
        org.junit.Assert.assertNotNull(deserialized.getFoo7());
        org.junit.Assert.assertNotNull(deserialized.isFoo8());
        org.junit.Assert.assertNotNull(deserialized.getFoo9());
    }
}