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

package com.cedarsoft.serialization.test.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.test.utils.ByTypeSource;

/**
 * Abstract base class for serializer tests.
 *
 * @param <T> the type of domain object
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractSerializerTest2<T> {

  private final boolean reflectionEqualsCheckEnabled;

  protected AbstractSerializerTest2() {
    this(false);
  }

  protected AbstractSerializerTest2(boolean reflectionEqualsCheckEnabled) {
    this.reflectionEqualsCheckEnabled = reflectionEqualsCheckEnabled;
  }

  @ParameterizedTest
  @MethodSource("provideData")
  @ByTypeSource(type = Entry.class)
  public void testSerializer( @Nonnull Entry<?> entry ) throws Exception {
    Serializer<T, OutputStream, InputStream> serializer = getSerializer();

    //Serialize
    @SuppressWarnings("unchecked")
    T object = (T) entry.getObject();
    @SuppressWarnings("unchecked")
    byte[] serialized = serialize(serializer, object);

    //Verify
    @SuppressWarnings("unchecked")
    Entry<T> entryCast = (Entry<T>) entry;
    verifySerialized(entryCast, serialized );

    verifyDeserialized(serializer.deserialize( new ByteArrayInputStream( serialized ) ), object);
  }

  @Nonnull
  protected byte[] serialize( @Nonnull Serializer<T, OutputStream, InputStream> serializer, @Nonnull T objectToSerialize ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    serializer.serialize( objectToSerialize, out );
    return out.toByteArray();
  }

  /**
   * Provides some test data.
   * May be overridden by sub classes
   */
  @Nonnull
  protected Stream<? extends Entry<? extends T>> provideData() {
    return Stream.empty();
  }

  protected abstract void verifySerialized( @Nonnull Entry<T> entry, @Nonnull byte[] serialized ) throws Exception;

  /**
   * Returns the serializer
   *
   * @return the serializer
   */
  @Nonnull
  protected abstract Serializer<T, OutputStream, InputStream> getSerializer() throws Exception;

  /**
   * Verifies the deserialized object
   *
   * @param deserialized the deserialized object
   * @param original     the original
   */
  protected void verifyDeserialized( @Nonnull T deserialized, @Nonnull T original ) {
    assertEquals( original, deserialized );
    if (reflectionEqualsCheckEnabled) {
      assertThat( deserialized, is( new ReflectionEquals( original ) ) );
    }
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull byte[] expected ) {
    return new Entry<>(object, expected);
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull URL expected ) {
    if (expected == null) {
      throw new IllegalArgumentException("No expected ULR found");
    }

    try {
      return new Entry<>(object, IOUtils.toByteArray(expected.openStream()));
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull InputStream expected ) {
    try {
      return new Entry<>(object, IOUtils.toByteArray(expected));
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
