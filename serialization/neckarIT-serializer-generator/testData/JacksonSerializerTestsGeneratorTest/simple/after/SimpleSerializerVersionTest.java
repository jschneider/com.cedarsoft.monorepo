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

public class SimpleSerializerVersionTest extends it.neckar.open.serialization.test.utils.AbstractJsonVersionTest2<Simple> {
    @NotNull
    @org.junit.experimental.theories.DataPoint
    public static final it.neckar.open.serialization.test.utils.VersionEntry ENTRY1 = it.neckar.open.serialization.test.utils.AbstractJsonVersionTest2.create(
            it.neckar.open.version.Version.valueOf(1, 0, 0), SimpleSerializerVersionTest.class.getResource("Simple_1.0.0_1.json"));

    @NotNull
    @Override
    protected it.neckar.open.serialization.StreamSerializer<Simple> getSerializer() throws Exception {
        return com.google.inject.Guice.createInjector().getInstance(SimpleSerializer.class);
    }

    @Override
    protected void verifyDeserialized(@NotNull Simple deserialized, @NotNull it.neckar.open.version.Version version) {
        org.junit.Assert.assertNotNull(deserialized.getFoo());
    }
}
