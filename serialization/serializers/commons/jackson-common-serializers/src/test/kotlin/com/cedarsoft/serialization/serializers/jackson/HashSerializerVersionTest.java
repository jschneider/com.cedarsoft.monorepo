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

import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.crypt.Algorithm;
import it.neckar.open.crypt.Hash;

import it.neckar.open.serialization.test.utils.AbstractJsonVersionTest2;
import it.neckar.open.serialization.test.utils.VersionEntry;

import it.neckar.open.version.Version;

public class HashSerializerVersionTest extends AbstractJsonVersionTest2<Hash> {

  public static final VersionEntry ENTRY1 = HashSerializerVersionTest.create( Version.valueOf( 1, 0, 0 ), HashSerializerVersionTest.class.getResource( "Hash_1.0.0_1.json" ) );

  @Nonnull
  @Override
  protected HashSerializer getSerializer() throws Exception {
    return new HashSerializer();
  }

  @Override
  protected void verifyDeserialized( @Nonnull Hash deserialized, @Nonnull Version version )
    throws Exception {
    Assertions.assertEquals(Algorithm.MD5, deserialized.getAlgorithm());
    Assertions.assertEquals("HASH", new String(deserialized.getValue(), StandardCharsets.UTF_8));
  }

}
