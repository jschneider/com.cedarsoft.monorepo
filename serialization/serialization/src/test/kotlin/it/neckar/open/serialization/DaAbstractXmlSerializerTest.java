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

package it.neckar.open.serialization;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionException;
import it.neckar.open.version.VersionRange;

/**
 *
 */
public class DaAbstractXmlSerializerTest {
  private MySerializer serializer;

  @BeforeEach
  public void setUp() throws Exception {
    serializer = new MySerializer();
  }

  @Test
  public void testNs() throws Exception {
    serializer.verifyNamespace( "nsBase" );
    serializer.verifyNamespace( "nsBaseADDITIONAL" );

    try {
      serializer.verifyNamespace("WrongnsBaseWRONG");
      fail("Where is the Exception");
    }
    catch (SerializationException e) {
      assertThat(e).hasMessage("[INVALID_NAME_SPACE] Invalid name space. Expected <nsBase/1.0.0> but was <WrongnsBaseWRONG>.");
    }
  }

  @Test
  public void testVerifyNsException() throws Exception {
    serializer.getFormatVersion();

    try {
      serializer.verifyNamespace( "WrongnsBaseWRONG" );
      fail( "Where is the Exception" );
    } catch ( SerializationException e ) {
      assertThat( e ).hasMessage( "[INVALID_NAME_SPACE] Invalid name space. Expected <nsBase/1.0.0> but was <WrongnsBaseWRONG>." );
    }
  }

  public static class MySerializer extends AbstractXmlSerializer<String, StringBuffer, String> {
    public MySerializer() {
      super( "mu", "nsBase", VersionRange.single( 1, 0, 0 ) );
    }

    @Override
    public void serialize(@Nonnull StringBuffer serializeTo, @Nonnull String objectToSerialize, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
      serializeTo.append(objectToSerialize);
    }

    @Nonnull
    @Override
    public String deserialize( @Nonnull String deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
      return deserializeFrom;
    }

    @Override
    public void serialize(@Nonnull String objectToSerialize, @Nonnull OutputStream out ) throws IOException {
      out.write(objectToSerialize.getBytes(StandardCharsets.UTF_8) );
    }

    @Nonnull
    @Override
    public String deserialize( @Nonnull InputStream deserializeFrom) throws IOException, VersionException {
      throw new UnsupportedOperationException();
    }
  }

}
