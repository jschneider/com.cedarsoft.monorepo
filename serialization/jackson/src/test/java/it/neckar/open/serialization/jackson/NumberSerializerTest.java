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

package it.neckar.open.serialization.jackson;

import static org.assertj.core.api.Fail.*;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.serialization.test.utils.AbstractJsonSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;

import it.neckar.open.test.utils.JsonUtils;

import it.neckar.open.version.Version;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 *
 */
public class NumberSerializerTest extends AbstractJsonSerializerTest2<Number> {
  @Override
  protected boolean addTypeInformation() {
    return false;
  }

  @Nonnull
  @Override
  protected NumberSerializer getSerializer() throws Exception {
    return new NumberSerializer();
  }

  @Test
  public void testNotClose() throws Exception {
    final boolean[] shallAcceptClose = {false};

    JsonFactory jsonFactory = JacksonSupport.getJsonFactory();
    OutputStream out = new FilterOutputStream( new ByteArrayOutputStream() ) {
      private boolean closed;

      @Override
      public void close() throws IOException {
        if ( !shallAcceptClose[0] ) {
          fail( "Unacceptable close!" );
        }

        super.close();
        closed = true;
      }
    };

    getSerializer().serialize( 123, out );
    shallAcceptClose[0] = true;
    out.close();
  }

  @Test
  public void testInt() throws Exception {
    JsonFactory jsonFactory = JacksonSupport.getJsonFactory();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonGenerator generator = jsonFactory.createGenerator( out, JsonEncoding.UTF8 );

    getSerializer().serialize( generator, 11133, Version.valueOf( 1, 0, 0 ) );

    generator.close();
    JsonUtils.assertJsonEquals("11133", out.toString());
  }

  @Test
  public void testDouble() throws Exception {
    JsonFactory jsonFactory = JacksonSupport.getJsonFactory();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonGenerator generator = jsonFactory.createGenerator( out, JsonEncoding.UTF8 );
    getSerializer().serialize( generator, 11133.4, Version.valueOf( 1, 0, 0 ) );
    generator.close();
    JsonUtils.assertJsonEquals("11133.4", out.toString());
  }

  @Test
  public void testDouble2() throws Exception {
    JsonFactory jsonFactory = JacksonSupport.getJsonFactory();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonGenerator generator = jsonFactory.createGenerator( out, JsonEncoding.UTF8 );
    getSerializer().serialize( generator, 11133.0, Version.valueOf( 1, 0, 0 ) );
    generator.close();
    JsonUtils.assertJsonEquals("11133.0", out.toString());
  }

  public static final Entry<?> ENTRY1 = create( 123, "123" );
  public static final Entry<?> ENTRY2 = create( 123.5, "123.5" );
  public static final Entry<?> ENTRY3 = create( 123.0, "123.0" );
}
