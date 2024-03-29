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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;

import it.neckar.open.test.utils.JsonUtils;

import it.neckar.open.version.Version;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

/**
 *
 */
public class NullSerializerTest {
  @Nonnull
  protected NullSerializer getSerializer() throws Exception {
    return new NullSerializer();
  }

  @Test
  public void testIt() throws Exception {
    JsonFactory jsonFactory = JacksonSupport.getJsonFactory();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonGenerator generator = jsonFactory.createGenerator( out, JsonEncoding.UTF8 );

    NullSerializer serializer = getSerializer();
    serializer.serialize( generator, null, Version.valueOf( 1, 0, 0 ) );
    generator.close();
    JsonUtils.assertJsonEquals("null", out.toString());

    assertNull( serializer.deserialize( new ByteArrayInputStream( out.toByteArray() ) ) );
  }

  @Test
  public void testStep() throws Exception {
    JsonParser parser = JacksonSupport.getJsonFactory().createParser( new ByteArrayInputStream( "null".getBytes(StandardCharsets.UTF_8) ) );
    assertNull( getSerializer().deserialize( parser ) );
    JacksonParserWrapper parserWrapper = new JacksonParserWrapper( parser );
    if ( parserWrapper.nextToken() != null ) {
      throw new JsonParseException(parser, "No consumed everything " + parserWrapper.getCurrentToken(), parserWrapper.getCurrentLocation());
    }

    parserWrapper.close();
  }
}
